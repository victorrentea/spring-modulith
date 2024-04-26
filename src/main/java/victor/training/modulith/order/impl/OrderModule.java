package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.api.order.OrderStatus;
import victor.training.modulith.shared.api.order.events.PaymentCompletedEvent;
import victor.training.modulith.shared.api.shipping.ShippingModuleApi;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderModule {
  private final OrderRepo orderRepo;
  private final ShippingModuleApi shippingModule;

  @EventListener
  public void onPaymentCompleted(PaymentCompletedEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }

}
