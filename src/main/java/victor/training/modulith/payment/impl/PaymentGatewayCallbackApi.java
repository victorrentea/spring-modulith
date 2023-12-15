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
import victor.training.modulith.payment.PaymentCompletedEvent;
import victor.training.modulith.shipping.ShippingModule;

@RestController
@RequiredArgsConstructor
public class PaymentGatewayCallbackApi {
//  private final OrderRepo orderRepo;
  private final ApplicationEventPublisher eventPublisher;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    eventPublisher.publishEvent(new PaymentCompletedEvent(orderId, ok));
    return "Payment callback received";
  }
}
