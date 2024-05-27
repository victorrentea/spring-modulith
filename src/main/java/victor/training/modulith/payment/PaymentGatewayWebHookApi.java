package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
//  private final OrderModuleApi orderModule;
  private final ApplicationEventPublisher eventPublisher;
  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
//    orderModule.onOrderPaid(orderId, ok);
    eventPublisher.publishEvent(new OrderPaidEvent(orderId, ok));
    System.out.println("Exit");
    return "Payment callback received";
  }
}
