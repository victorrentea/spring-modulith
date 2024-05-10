package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.CatalogModuleInterface;
import victor.training.modulith.order.InventoryModuleInterface;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.payment.OrderPaidEvent;
import victor.training.modulith.payment.PaymentService;
import victor.training.modulith.shared.LineItem;
import victor.training.modulith.shipping.in.api.ShippingModuleApi;
import victor.training.modulith.shipping.out.event.ShippingResultEvent;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static victor.training.modulith.order.impl.OrderRestApi.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepo orderRepo;
  private final CatalogModuleInterface catalogModule;
  private final InventoryModuleInterface inventoryModule;
  private final PaymentService paymentService;
  private final ShippingModuleApi shippingModule;

  public Order getOrder(long orderId) {
    return orderRepo.findById(orderId).orElseThrow();
  }

  public String doPlaceOrder(PlaceOrderRequest request) {
    List<Long> productIds = request.items().stream().map(LineItem::productId).toList();
    Map<Long, Double> prices = catalogModule.getManyPrices(productIds);
    Map<Long, Integer> items = request.items().stream().collect(toMap(LineItem::productId, LineItem::count));
    double totalPrice = request.items().stream().mapToDouble(e -> e.count() * prices.get(e.productId())).sum();
    Order order = new Order()
        .items(items)
        .shippingAddress(request.shippingAddress())
        .customerId(request.customerId())
        .total(totalPrice);
    orderRepo.save(order);
    inventoryModule.reserveStock(order.id(), request.items());
    return paymentService.generatePaymentUrl(order.id(), order.total());
  }

  @ApplicationModuleListener
  public void onOrderPaid(OrderPaidEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }

  @ApplicationModuleListener
  public void onShippingResultEvent(ShippingResultEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.shipped(event.ok());
    orderRepo.save(order); // without @Test fails, as events not published
  }
}
