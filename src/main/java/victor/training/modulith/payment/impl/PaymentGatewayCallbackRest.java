package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.customer.CustomerModule;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.payment.PaymentResultEvent;

@RestController
@RequiredArgsConstructor
public class PaymentGatewayCallbackRest {
  private final ApplicationEventPublisher eventPublisher;

  @PutMapping("order/{orderId}/confirm-payment")
  public void confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    eventPublisher.publishEvent(new PaymentResultEvent(orderId, ok));
  }
}
