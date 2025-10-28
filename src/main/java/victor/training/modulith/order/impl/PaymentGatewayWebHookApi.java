package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.shipping.ShippingInternalApi;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = HTTP call back to me from a third party
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final OrderService orderService;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    return orderService.confirmPay(orderId, ok);
  }


}
