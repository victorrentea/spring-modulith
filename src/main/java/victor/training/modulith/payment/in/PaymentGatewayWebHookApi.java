package victor.training.modulith.payment.in;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.OrderInternalApi;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.shipping.ShippingInternalApi;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = HTTP call back to me from a third party
public class PaymentGatewayWebHookApi {
  private final OrderInternalApi orderInternalApi; // TODO move to 'payment' module

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    orderInternalApi.orderLogic(orderId, ok);
    return "Payment callback received";
  }

  // how to eliminate a dep cycle between 2 modules?
  // A) PaymentCompletedEvent from payment->order
  // B) push to FE/BFF fetch(POST order).then(fetch(GET payment-url))
  // C) dependency inversion via an interface: order exposed, payment implements

}
