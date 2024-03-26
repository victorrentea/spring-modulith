package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.payment.PaymentCompletedEvent;
import victor.training.modulith.shipping.in.api.ShippingModule;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentStatusHandler {
  private final OrderRepo orderRepo;
  private final ShippingModule shippingModule;

  @EventListener
//  @Async (don't have the publisher wait)
//  @Transactional(propagation = Propagation.REQUIRES_NEW) // have a separate inner tx
//  @ApplicationModuleListener // from spring Modulith: the events are persisted in DB while waiting to start processing
  public void processPayment(PaymentCompletedEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
