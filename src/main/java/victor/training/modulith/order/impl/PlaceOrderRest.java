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
import victor.training.modulith.order.CatalogModuleApi;
import victor.training.modulith.order.InventoryModuleApi;
import victor.training.modulith.order.OrderStatusChangedEvent;
import victor.training.modulith.payment.PaymentModule;
import victor.training.modulith.shared.LineItem;
import victor.training.modulith.shipping.ShippingResultEvent;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PlaceOrderRest {
  private final OrderRepo orderRepo;
  private final CatalogModuleApi catalogModuleApi;
  private final InventoryModuleApi inventoryModuleApi;
  private final PaymentModule paymentModule;

  public record PlaceOrderRequest(@NotEmpty String customerId, @NotEmpty List<LineItem> items, @NotEmpty String shippingAddress) {
  }

  @PostMapping("order")
  public String placeOrder(@RequestBody @Validated PlaceOrderRequest request) {
    List<Long> productIds = request.items().stream().map(LineItem::productId).toList();
    Map<Long, Double> prices = catalogModuleApi.getManyPrices(productIds);
    Map<Long, Integer> items = request.items.stream().collect(toMap(LineItem::productId, LineItem::count));
    double totalPrice = request.items.stream().mapToDouble(e -> e.count() * prices.get(e.productId())).sum();
    Order order = new Order()
        .items(items)
        .shippingAddress(request.shippingAddress)
        .customerId(request.customerId)
        .total(totalPrice);
    orderRepo.save(order);
    inventoryModuleApi.reserveStock(order.id(), request.items);
    return paymentModule.generatePaymentUrl(order.id(), order.total());
  }

  @EventListener
  public void justLog(Object event) {
    if (event.getClass().getPackageName().contains("victor")) {
      log.debug("Event published : " +event);
    }
  }

  @ApplicationModuleListener
  public void onShippingResultEvent(ShippingResultEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.shipped(event.ok());
    orderRepo.save(order); // without @Test fails, as events not published
  }
}
