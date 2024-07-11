package victor.training.modulith.order.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.payment.PaymentCompletedEvent;
import victor.training.modulith.shipping.ShippingInternalApi;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCompletedEventListener {
  private final OrderRepo orderRepo;
  private final InventoryInternalApi inventoryInternalApi;
  private final ShippingInternalApi shippingInternalApi;
//    registry.register(PaymentCompletedEvent, this::onPaymentConfirmed)

  // preparing to eject a module as a microservice.
//  @Async // I had a problem and I wanted to use multithreading. Two problems have I now. but it;s closer to distributed system
  @EventListener // in memory event bus // observer pattern
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // in memory event bus // observer pattern
  public void onPaymentConfirmed(PaymentCompletedEvent event) {
    log.info("Received PaymentCompletedEvent: " + event);
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.pay(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      inventoryInternalApi.confirmReservation(order.id());
      String trackingNumber = shippingInternalApi.requestShipment(order.id(), order.shippingAddress());
      order.wasScheduleForShipping(trackingNumber);
    }
//    if (true) throw new RuntimeException("Intentional");
    orderRepo.save(order);
  }




//  {
//    db.insert(stuff);
//    queue.send(event);
//  }
//  {
//    db.insert(stuff);
//    queue.send(event);
//  }
}
