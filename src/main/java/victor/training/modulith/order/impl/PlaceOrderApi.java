package victor.training.modulith.order.impl;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.catalog.CatalogInternalApi;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.OrderInternalApi;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.payment.PaymentConfirmedEvent;
import victor.training.modulith.payment.PaymentService;
import victor.training.modulith.shared.LineItem;
import victor.training.modulith.shipping.ShippingInternalApi;
import victor.training.modulith.shipping.ShippingResultEvent;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlaceOrderApi {
  private final OrderRepo orderRepo;
  private final CatalogInternalApi catalogInternalApi;
  private final InventoryInternalApi inventoryInternalApi;
  private final PaymentService paymentUrlGenerator;

  public record PlaceOrderRequest(
      @NotEmpty String customerId,
      @NotEmpty List<LineItem> items,
      @NotEmpty String shippingAddress) {
  }

  @PostMapping("order")
  public String placeOrder(@RequestBody @Validated PlaceOrderRequest request) {
    List<Long> productIds = request.items().stream().map(LineItem::productId).toList();
    Map<Long, Double> prices = catalogInternalApi.getManyPrices(productIds);
    Map<Long, Integer> items = request.items.stream().collect(toMap(LineItem::productId, LineItem::count));
    double totalPrice = request.items.stream().mapToDouble(e -> e.count() * prices.get(e.productId())).sum();
    Order order = new Order()
        .items(items)
        .shippingAddress(request.shippingAddress)
        .customerId(request.customerId)
        .total(totalPrice);
    orderRepo.save(order);
    inventoryInternalApi.reserveStock(order.id(), request.items);
    return paymentUrlGenerator.generatePaymentUrl(order.id(), order.total());
  }

  @ApplicationModuleListener
  public void onShippingResultEvent(ShippingResultEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.wasShipped(event.ok());
    orderRepo.save(order); // without @Test fails, as events not published
  }

  private final ShippingInternalApi shippingInternalApi;

  @EventListener //by default
  // all listeners are called synchronusly 
  // in the same tx as publisher
  // DO not use @Order annotation on them!
  public void onPaymentConfirmed(PaymentConfirmedEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.pay(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      inventoryInternalApi.confirmReservation(order.id());
      String trackingNumber = shippingInternalApi.requestShipment(order.id(), order.shippingAddress());
      order.wasScheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
