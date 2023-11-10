package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.CatalogDoor;
import victor.training.modulith.order.InventoryDoor;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.payment.PaymentResultEvent;
import victor.training.modulith.payment.PaymentModule;
import victor.training.modulith.common.LineItem;
import victor.training.modulith.common.ProductId;
import victor.training.modulith.shipping.ShippingModule;
import victor.training.modulith.shipping.ShippingResultEvent;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlaceOrderRest {
  private final OrderRepo orderRepo;
  private final CatalogDoor catalogDoor;
  private final PaymentModule paymentModule;
  private final InventoryDoor inventoryDoor;
  private final ShippingModule shippingDoor;

  record PlaceOrderRequest(String customerId, List<LineItem> items, String shippingAddress) {
  }

  @PostMapping("order")
  public String placeOrder(@RequestBody PlaceOrderRequest request) {
    List<ProductId> productIds = request.items().stream().map(LineItem::productId).toList();
    Map<ProductId, Double> prices = catalogDoor.getManyPrices(productIds);
    Map<ProductId, Integer> items = request.items.stream().collect(toMap(LineItem::productId, LineItem::count));
    double totalPrice = request.items.stream().mapToDouble(e -> e.count() * prices.get(e.productId())).sum();
    Order order = new Order()
        .items(items)
        .shippingAddress(request.shippingAddress)
        .customerId(request.customerId)
        .total(totalPrice);
    orderRepo.save(order);
    inventoryDoor.reserveStock(order.id(), request.items);
    return paymentModule.generatePaymentUrl(order.id(), order.total());
  }

  @ApplicationModuleListener
  public void onPaymentResultEvent(PaymentResultEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingDoor.requestShipment(order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
  }

  @ApplicationModuleListener
  public void onShippingResultEvent(ShippingResultEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.shipped(event.ok());
  }
}
