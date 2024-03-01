package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.payment.PaymentProcessedEvent;
import victor.training.modulith.shipping.in.api.ShippingModule;

@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final ApplicationEventPublisher eventPublisher;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    eventPublisher.publishEvent(new PaymentProcessedEvent(orderId, ok));
    System.out.println("Exit");
    return "Payment callback received";
  }
}
