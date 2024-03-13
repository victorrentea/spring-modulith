package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.shared.events.payment.PaymentCompletedEvent;

@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(
      @PathVariable long orderId,
      @RequestBody boolean ok) {
    eventPublisher.publishEvent(new PaymentCompletedEvent(orderId, ok));
    return "Payment callback received";
  }
  private final ApplicationEventPublisher eventPublisher;
}
