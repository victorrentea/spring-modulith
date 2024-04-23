package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.payment.PaymentFinishedEvent;
import victor.training.modulith.shipping.in.api.ShippingModuleApi;

@Service
@RequiredArgsConstructor
public class PaymentFinishedListener {
  private final OrderRepo orderRepo;
  private final ShippingModuleApi shippingModule;

  @EventListener
  public void onPaymentFinished(PaymentFinishedEvent event) {
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
