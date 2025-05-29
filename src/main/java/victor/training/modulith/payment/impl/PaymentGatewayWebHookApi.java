package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.OrderInternalApi;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = HTTP call back to me from a third party
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final OrderInternalApi orderInternalApi;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    orderInternalApi.confirmPayment(orderId, ok);
    return "Payment callback received";
  }
  // order.PlaceOrderApi -> PaymentService.getPayUrl
  // order.cofirmPayment <- PaymentWebHook.call
  // ways to fix this cycle:
  // >> a) Dependency Inversion (via a new interface)
  // >> b) Event: payment->PaymentConfirmationEvent->order
  // X c) Orchestrate From Above: Facade module to call PlaceOrder, then getPayUrl
  // c-fullstack) fetch('placeorder').then(r=>fetch('paymenturl))
  // x d) 💖 Merge back the two modules; oups: wrong boundary
  // >>>> e) Segregate module contracts => no cycles are possible ⭐️
}
