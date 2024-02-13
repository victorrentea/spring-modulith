package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.payment.PaymentStatusListener;

@RestController
@RequiredArgsConstructor
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final PaymentStatusListener paymentListener;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    // by default Spring dispatches this event synchronously to all listeners
//    eventPublisher.publishEvent(new PaymentConfirmedEvent(orderId, ok));
    paymentListener.paymentConfirmed(orderId, ok);
//    orderRestApi.confirmPayment(orderId); // REST call

    // here all listeners have executed successfully
    // problem: other teams write a listener that takes 1 second to complete, your request might timeout

    System.out.println("Exit");
    return "Payment callback received";
  }
}

