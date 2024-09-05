package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.OrderInternalApi;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
//  private final OrderInternalApi orderInternalApi;
  private final ApplicationEventPublisher eventPublisher;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
//    orderInternalApi.confirmPayment(orderId, ok);
    // un mesaj poate fi COMMAND(dest=1) sau EVENT(dest=0..N) pattern
    // rabbit(COMMAND) / kafka(EVENT) depinde.
    // t pt ca ar putea sa vrea sa stie si altii

    eventPublisher.publishEvent(new PaymentConfirmedEvent(orderId, ok));
    return "Payment callback received";
  }
}
