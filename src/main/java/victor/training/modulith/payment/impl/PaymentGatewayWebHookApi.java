package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.internalapi.OrderModuleApi;
import victor.training.modulith.payment.PaymentReceivedEvent;

@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
//  private final OrderModuleApi orderModuleApi;

  private final ApplicationEventPublisher eventPublisher;
  // in-memory events bus ( also via Guice/Guava, Quarkus,..)
  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    //orderModuleApi.onPaymentConfirmed(orderId, ok);
    var event = new PaymentReceivedEvent(orderId, ok);
    eventPublisher.publishEvent(event); // all the @EventListener methods will be called
    // in my thread and in any @Transactional i might be in
    return "Payment callback received";
  }


}
