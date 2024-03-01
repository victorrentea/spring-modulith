package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.payment.PaymentProcessedEvent;
import victor.training.modulith.shipping.in.api.ShippingModule;

@Component
@RequiredArgsConstructor
public class PaymentProcessedEventListener {
  private final ShippingModule shippingModule;
  private final OrderRepo orderRepo;
  @EventListener(PaymentProcessedEvent.class)
  public void onPaymentProcessed(PaymentProcessedEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }

}
