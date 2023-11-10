package victor.training.modulith.payment.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.payment.PaymentResultEvent;

@RestController
@RequiredArgsConstructor
public class PaymentGatewayCallbackRest {
//  private final OrderModule orderModule;
  private final ApplicationEventPublisher eventPublisher;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId,
                               @RequestBody boolean ok) {
//    orderModule.onPaymentProcessed(orderId, ok);
    // daca nu am nevoie de rezultate si nu tre sa astept sa termine => eventuri
    eventPublisher.publishEvent(new PaymentResultEvent(orderId, ok));
    return "Payment callback received";
  }
}
