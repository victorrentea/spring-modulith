package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.impl.repo.OrderRepo;
import victor.training.modulith.payment.PaymentConfirmedEvent;
import victor.training.modulith.shipping.in.api.ShippingModule;

@Component
@RequiredArgsConstructor
public class PaymentConfirmedEventListener {
  private final OrderRepo orderRepo;
  private final ShippingModule shippingModule;

  @EventListener
//  @Async
//  @ApplicationModuleListener // == stores the event in a DB table while waiting
  public void onPaymentConfirmed(PaymentConfirmedEvent event) {
     Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
    System.out.println("Exit");
  }
}
