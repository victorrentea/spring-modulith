package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.api.order.OrderStatus;
import victor.training.modulith.shared.api.payment.PaymentConfirmationEvent;
import victor.training.modulith.shared.api.inventory.InventoryInternalApi;
import victor.training.modulith.shared.api.shipping.ShippingInternalApi;

@Service
@RequiredArgsConstructor
public class OrderInternalApiImpl implements victor.training.modulith.shared.api.order.OrderInternalApi {

  private final InventoryInternalApi inventoryInternalApi;
  private final OrderRepo orderRepo;
  private final ShippingInternalApi shippingInternalApi;

//  public void confirmPayment(long orderId, boolean ok) {
  @EventListener
//@ApplicationModuleListener
@Override
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
