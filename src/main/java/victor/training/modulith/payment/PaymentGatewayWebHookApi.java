package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP from the Payment Gatway when the payment is completed
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final PaymentInterface paymentInterface;
  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    paymentInterface.processPayment(orderId, ok);
    System.out.println("Exit");
    return "Payment callback received";
  }
}
