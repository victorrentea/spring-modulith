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
import victor.training.modulith.payment.PaymentProcessedEvent;
import victor.training.modulith.shipping.in.api.ShippingInternalApi;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Service
@RequiredArgsConstructor
public class OrderInternalApi {
  private final OrderRepo orderRepo;
  private final InventoryInternalApi inventoryInternalApi;
  private final ShippingInternalApi shippingInternalApi;

  @EventListener // cand ruleaza asta ????????????
  // din ce in ce mai aproape de realitatea microserviciilor:
//  @EventListener @Async // probleme doua Ai acum.
//  @TransactionalEventListener(phase = AFTER_COMMIT) @Async // probleme doua Ai acum.
//  @RabbitListener // probleme doua Ai acum.
  public void onPaymentProcessedEvent(PaymentProcessedEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      inventoryInternalApi.confirmReservation(order.id());
      String trackingNumber = shippingInternalApi.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }

}
