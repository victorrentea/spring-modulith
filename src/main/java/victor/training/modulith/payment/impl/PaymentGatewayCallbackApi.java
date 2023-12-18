package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.payment.PaymentCompletedEvent;

// this is a "webhook" is a callback from a 3rd party system to our system
@RestController
@RequiredArgsConstructor
public class PaymentGatewayCallbackApi { // TODO move to 'payment' module
//  private final OrderModule orderModule;
  private final ApplicationEventPublisher eventPublisher;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
//    orderModule.setOrderPaid(orderId, ok); // i don't a need a result back
  eventPublisher.publishEvent(new PaymentCompletedEvent(orderId, ok));
    System.out.println("Exit");
    return "Payment callback received";
  }
}
