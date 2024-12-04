package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.payment.PaymentConfirmationEvent;
import victor.training.modulith.shipping.ShippingInternalApi;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Service
@RequiredArgsConstructor
public class OrderInternalApi {
  private final OrderRepo orderRepo;
  private final InventoryInternalApi inventoryInternalApi;
  private final ShippingInternalApi shippingInternalApi;

  @EventListener
//  @TransactionalEventListener(phase = AFTER_COMMIT)
//  @Async
  public void onPaymentConfirmation(PaymentConfirmationEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.pay(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      inventoryInternalApi.confirmReservation(order.id());
      String trackingNumber = shippingInternalApi.requestShipment(order.id(), order.shippingAddress());
      order.wasScheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
//    sleep(10000) // intarzii publisher
    // throw arunc in publisher
    // rollback la tx care-mi vine de la publisher
  }


}
