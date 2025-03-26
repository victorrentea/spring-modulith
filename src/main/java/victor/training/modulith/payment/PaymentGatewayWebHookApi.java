package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi {
  private final ApplicationEventPublisher eventPublisher; // TODO move to 'payment' module

  @PutMapping("payment/{orderId}/status")
  @Transactional
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    // by default spring will call all listeners blocking the publisher
    // sharing the thread and transaction (if any)
    eventPublisher.publishEvent(new PaymentCompleted(orderId, ok));
    return "Payment callback received";
  }


}

