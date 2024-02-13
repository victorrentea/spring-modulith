package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.payment.PaymentConfirmedEvent;
import victor.training.modulith.shipping.in.api.ShippingModule;

@RequiredArgsConstructor
@Service
public class PaymentConfirmedListener {
  private final OrderRepo orderRepo;
  private final ShippingModule shippingModule;

  @EventListener // default: sync, in this tread, sharing the publisher's transaction

  // dangerous:
//  @ApplicationModuleListener // async and separate transactional,
  // events can be persisted to a db while waiting (not to lose them)
  public void onPaymentConfirmed(PaymentConfirmedEvent event) {
//    System.out.println("Payment confirmed for order " + event.getOrderId());
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
