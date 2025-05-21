package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.api.inventory.InventoryInternalApi;
import victor.training.modulith.shared.api.order.OrderStatus;
import victor.training.modulith.shared.api.payment.PaymentCompletedEvent;
import victor.training.modulith.shared.api.shipping.ShippingInternalApi;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventHandler {
  private final OrderRepo orderRepo;
  private final ShippingInternalApi shippingInternalApi;
  private final InventoryInternalApi inventoryInternalApi;

//  @Async // puts you in a memory waiting queue if all 10 worker threads are busy
  // k8s kill-9s you =>loose data
  // @simon said: why not set a queue of 0 => .publish will crash to Payment webhook call
//  @EventListener
  @ApplicationModuleListener // are persisted in your SQL/Kafka db until they are handled
  // in one DB table.
  // Fun fact.
  public void onPaymentCompleted(PaymentCompletedEvent event) {
    log.info("in order payment handler");
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.pay(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      inventoryInternalApi.confirmReservation(order.id());
      String trackingNumber = shippingInternalApi.requestShipment(order.id(), order.shippingAddress());
      order.wasScheduleForShipping(trackingNumber);
    } else {
      inventoryInternalApi.cancelReservation(order.id());
    }
    orderRepo.save(order);
  }
}
