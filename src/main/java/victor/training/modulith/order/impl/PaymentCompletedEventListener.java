package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.payment.PaymentCompletedEvent;
import victor.training.modulith.shipping.ShippingModule;

@Component
@RequiredArgsConstructor
public class PaymentCompletedEventListener {
  private final OrderRepo orderRepo;
  private final ShippingModule shippingModule;

  @EventListener
  public void onPaymentCompleted(PaymentCompletedEvent event) {
    Order order = orderRepo.findById(event.paymentId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
