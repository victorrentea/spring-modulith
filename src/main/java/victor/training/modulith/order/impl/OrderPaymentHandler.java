package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.payment.PaymentCompletedEvent;
import victor.training.modulith.shipping.ShippingInternalApi;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPaymentHandler {
  private final OrderRepo orderRepo;
  private final InventoryInternalApi inventoryInternalApi;
  private final ShippingInternalApi shippingInternalApi;

  // this is MUCH LESS MAINTAINABLE. DO NOT USE EVENTS
  // unless you plan to cut out one of the modules as a separate micro service
  // preparing the ground for using Kafka or RabbitMQ
  @EventListener
  public void onPaymentCompleted(PaymentCompletedEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.pay(event.success());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      inventoryInternalApi.confirmReservation(order.id());
      String trackingNumber = shippingInternalApi.requestShipment(order.id(), order.shippingAddress());
      order.wasScheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
