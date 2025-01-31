package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.payment.PaymentCompletedEvent;
import victor.training.modulith.shipping.ShippingInternalApi;

@Service
@RequiredArgsConstructor
public class OrderInternalApi {
  private final OrderRepo orderRepo;
  private final InventoryInternalApi inventoryInternalApi;
  private final ShippingInternalApi shippingInternalApi;

  @EventListener
//  @org.springframework.core.annotation.Order // bad practice as it represents a global coupling point
  public void onPaymentCompleted(PaymentCompletedEvent event) {
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
