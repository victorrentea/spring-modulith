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
  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId,
                               @RequestBody boolean ok) {
//    orderInternalApi.onPaymentConfirmed(orderId, ok);
    // trigger an event.
    // and mind that you don't loose your events on server crash
    // so send them on a durable queue(rabbit)/topic(kafka)
    // so they stay on disk until the order module picks them up
    eventPublisher.publishEvent(new PaymentConfirmedEvent(orderId, ok));

    return "Payment callback received";
  }

  // KafkaTemplate<String, String> kafkaTemplate;
  // RabbitTemplate rabbitTemplate;
  private final ApplicationEventPublisher eventPublisher;

}
