package victor.training.modulith.api;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.catalog.CatalogInternalApi;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.OrderInternalApi;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.payment.PaymentService;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OldApi {
  private final OrderRepo orderRepo;
  private final CatalogInternalApi catalogInternalApi;
  private final InventoryInternalApi inventoryInternalApi;
  private final PaymentService urlPaymentGeneratorPort;
  private final OrderInternalApi orderInternalApi;
  private final PaymentService paymentService;

  public record PlaceOrderRequest(
      @NotEmpty String customerId,
      @NotEmpty List<LineItem> items,
      @NotEmpty String shippingAddress) {
  }

  @PostMapping("order")
  public String placeOrder(@RequestBody @Validated victor.training.modulith.order.impl.PlaceOrderApi.PlaceOrderRequest request) {
    orderInternalApi.placeOrder(request);
    return paymentService.generatePaymentUrl(1L, 1.0);
  }
}