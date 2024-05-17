package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.payment.PaymentCompletedEvent;

@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PayPalGatewayWebHookApi { // TODO move to 'payment' module
  private final ApplicationEventPublisher eventPublisher; // spring

//  @Transactional
  @PutMapping("payment/{paymentId}/status")
  public String confirmPayment(@PathVariable long paymentId, @RequestBody boolean ok) {
    eventPublisher.publishEvent(new PaymentCompletedEvent(paymentId, ok));
    System.out.println("Exit");
    return "Payment callback received";
  }


}
