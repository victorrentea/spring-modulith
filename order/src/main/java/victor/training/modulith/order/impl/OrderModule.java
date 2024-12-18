package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.internalapi.order.OrderStatus;
import victor.training.modulith.shared.internalapi.order.events.PaymentCompletedEvent;
import victor.training.modulith.shared.internalapi.shipping.ShippingModuleApi;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderModule {
  private final OrderRepo orderRepo;
  private final ShippingModuleApi shippingModule;

  @EventListener
//  @Async
//  @ApplicationModuleListener// like async but saves the events that are waiting in the database (don;t use-it's draft)
  // use you Kafka. RabbitMQ, etc
  public void onPaymentCompleted (PaymentCompletedEvent event) {
    log.info("Received PaymentCompletedEvent: {}", event);
    // runs by deafult in the publisher's thread and blocks the publisher in its transaction (if any)
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }

}
