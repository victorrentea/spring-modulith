package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.payment.PaymentCompletedEvent;
import victor.training.modulith.shipping.in.api.ShippingModuleApi;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderModuleApi {
  private final OrderRepo orderRepo;
  private final ShippingModuleApi shippingModule;

  @EventListener //a) by default sync in tx publisher

  //  @EventListener @Async //b) have you Two now problems! (race conditions) listener might not find the record inserted by publisher as publsiher might not have committed yet

  //  @TransactionalEventListener(phase = AFTER_COMMIT) @Async //c) alta tx, dupa mine, dar async

//  @TransactionalEventListener(phase = AFTER_COMMIT) //d) sync cu mine, il astept, dar dupa comitu publisherului

//  @ApplicationModuleListener // e) async + persistat in DB intre timp; cam noua ca s-o pun prod
//  @KafkaListener
  public void onPaymentResult(PaymentCompletedEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
