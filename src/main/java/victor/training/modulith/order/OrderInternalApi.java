package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.payment.PaymentConfirmationEvent;
import victor.training.modulith.shipping.ShippingInternalApi;

@Service
@RequiredArgsConstructor
public class OrderInternalApi {
  private final OrderRepo orderRepo;
  private final InventoryInternalApi inventoryInternalApi;
  private final ShippingInternalApi shippingInternalApi;

  // AVOID until the publisher or listener want to "move out" = become a separate deploy ----> @KafkaListener
  // if the modular monolith is still comfortable today, use method calls, not events.
  @EventListener // is this executed async: NO! It's synchronous by default unlike a @KafkaListener
  // PANIC: if i throw an exception it is thrown in the publisher
  // PANIC: if i cause a Tx rollback, i rollback the publisher's Tx too
//  @Async // loose  hell  breaks
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//  @ApplicationModuleListener // spring-modulith: i'm going to persist the event until you proceess it
  public void onPaymentConfirmationEvent(PaymentConfirmationEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.pay(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      inventoryInternalApi.confirmReservation(order.id());
      String trackingNumber = shippingInternalApi.requestShipment(order.id(), order.shippingAddress());
      order.wasScheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
