package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
//import victor.training.modulith.order.OrderModule;
//import victor.training.modulith.order.OrderStatus;
//import victor.training.modulith.order.impl.Order;
//import victor.training.modulith.order.impl.repo.OrderRepo;
import victor.training.modulith.payment.PaymentConfirmedEvent;
import victor.training.modulith.shipping.in.api.ShippingModule;

@RestController
@RequiredArgsConstructor
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
//  private final OrderModule orderModule;
  private final ApplicationEventPublisher eventPublisher;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
//    orderModule.confirmPayment(orderId, ok);
    eventPublisher.publishEvent(new PaymentConfirmedEvent(orderId, ok)); // via an in-memory event bus
//    db.save()
    // if the onPaymentConfirmed throws an exception, the exception is thrown in the publisher
    // by default Spring dispatches the event to ALL listeners in the same thread using the same transaction of the publisher( if any)
    return "Payment callback received";
  }
}
