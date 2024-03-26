package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.payment.PaymentCompletedEvent;

@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP from the Payment Gatway when the payment is completed
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final ApplicationEventPublisher eventPublisher;

  @PutMapping("payment/{orderId}/status")
  @Transactional
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    eventPublisher.publishEvent(new PaymentCompletedEvent(orderId, ok));
    // by default Spring delivers the event
    // to all the listeners within the publisher thread
    // - blocking the publisher
    // - sharing the transaction with all listeners
    // I LOVE💖 this behavior = gives me strong consistency, impossible in a distributed system
    System.out.println("Exit");
    return "Payment callback received";
  }
}
