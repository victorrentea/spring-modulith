package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.payment.PaymentStatusListener;
import victor.training.modulith.shipping.in.api.ShippingModule;

@Service
@RequiredArgsConstructor
public class PaymentStatusListenerImpl implements PaymentStatusListener {
  private final OrderRepo orderRepo;
  private final ShippingModule shippingModule;

  public void paymentConfirmed(long orderId, boolean ok) {
    Order order = orderRepo.findById(orderId).orElseThrow();
    order.paid(ok);
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
