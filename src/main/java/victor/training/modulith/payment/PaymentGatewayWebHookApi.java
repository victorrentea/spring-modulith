package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final ApplicationEventPublisher eventPublisher;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
//    IOrderInternalApi.confirmPayment(orderId, ok);
//    eventPublisher.publishEvent(new PaymentEvent(orderId, ok)); // bad event name
//    eventPublisher.publishEvent(new PaymentStatusEventUpdated(orderId, ok)); // CUD; boring event
    eventPublisher.publishEvent(new PaymentCompletedEvent(orderId, ok)); // CUD; boring event
    return "Payment callback received";
  }
}
