package victor.training.modulith.order.impl;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.catalog.CatalogInternalApi;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.inventory.StockReservationRequestIDto;
import victor.training.modulith.order.PaymentProviderSPI;
import victor.training.modulith.shared.LineItem;
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
  private final PaymentProviderSPI paymentProviderSPI;

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
    inventoryInternalApi.reserveStock(new StockReservationRequestIDto(order.id(), request.items));
    return paymentProviderSPI.generatePaymentUrl(order.id(), order.total());
  }

  @ApplicationModuleListener
//  @EventListener
  public void onShippingResultEvent(ShippingResultEvent event) {
    log.info("Listened to {}", event);
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.wasShipped(event.ok());
    orderRepo.save(order); // without @Test fails, as events not published
    log.info("Listener completed");
  }
}
