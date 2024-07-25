package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.payment.PaymentCompletedEvent;

@RestController
@RequiredArgsConstructor
@Slf4j
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final ApplicationEventPublisher eventPublisher;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    log.info("publishing");
    eventPublisher.publishEvent(new PaymentCompletedEvent(orderId, ok));
    log.info("IShouldSeeThis if no exception, after all listeners finish");
    return "Payment callback received";
  }

}
