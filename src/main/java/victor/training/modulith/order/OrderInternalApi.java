package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.payment.PaymentConfirmationEvent;
import victor.training.modulith.shipping.ShippingInternalApi;

@Service
@RequiredArgsConstructor
public class OrderInternalApi {

  private final InventoryInternalApi inventoryInternalApi;
  private final OrderRepo orderRepo;
  private final ShippingInternalApi shippingInternalApi;

//  public void confirmPayment(long orderId, boolean ok) {
//  @EventListener
  @ApplicationModuleListener // ⭐️ async, DB-persisted events; different thread & transaction
  public void onPaymentConfirmed(PaymentConfirmationEvent event) {
    long orderId = event.orderId();
    boolean ok = event.ok();
    Order order = orderRepo.findById(orderId).orElseThrow();
    order.pay(ok);
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
