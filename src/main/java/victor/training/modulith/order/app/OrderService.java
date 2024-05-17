package victor.training.modulith.order.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.in.internal.OrderStatus;
import victor.training.modulith.payment.PaymentCompletedEvent;
import victor.training.modulith.shipping.in.api.ShippingModuleApi;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepo orderRepo;
  private final ShippingModuleApi shippingModule;

  // start this journey if Payment has to be extracted as a separate deployment (microservice)
  @EventListener // [by default] dispatched in thread ±transaction of publisher - we are pretending (just like Dep Inversion)
    //  @Order(1)// couples all listeners
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // same thread, second tx (after the first one is committed)
//  @EventListener @Async // all Help God you! = second thread, ofc 2nd tx
//  @ApplicationModuleListener // spring-modulith persists events transactionally in DB while waiting
//  @KafkaListener// sending a event to myself through an external system. overengineering unless extracting a microservice in next sprint.
  public void onOrderPaid(PaymentCompletedEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
