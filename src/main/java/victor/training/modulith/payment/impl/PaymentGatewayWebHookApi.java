package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.payment.PaymentCompletedEvent;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = HTTP call back to me from a third party
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final ApplicationEventPublisher applicationEventPublisher;

  @PutMapping("payment/{orderId}/status")
  @Transactional
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    var event = new PaymentCompletedEvent(orderId, ok);
//    paymentEvidenceRepo.save(new PaymentEvidence(blah)); // committed with the event insert
    applicationEventPublisher.publishEvent(event);
    return "Payment callback received";
  }
  // problems:
  // makes code harder to navigate/stacktrace
  // multiple listener. in what order?
  // are the listeners called async => NO.😱
  // all listeners are called sequentially in the thread & transaction of the publisher
  // any exception from a listener will others to run and bubble up in publisher


}
