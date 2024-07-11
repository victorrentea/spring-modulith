package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.payment.PaymentCompletedEvent;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final ApplicationEventPublisher eventPublisher;
//  @Transactional
  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) throws InterruptedException {
//    repo.save(something);
    eventPublisher.publishEvent(new PaymentCompletedEvent(orderId, ok));
//    Thread.sleep(10);
    log.info("After publishing");
    // restApi.call;
    return "Payment callback received";
  }

}
