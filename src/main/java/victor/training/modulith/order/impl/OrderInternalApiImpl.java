package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.api.shared.shipping.ShippingInternalApi;
import victor.training.modulith.shared.api.inventory.InventoryInternalApi;
import victor.training.modulith.shared.api.payment.PaymentCompletedEvent;
import victor.training.modulith.shared.api.order.OrderStatus;

@Service
@RequiredArgsConstructor
public class OrderInternalApiImpl implements victor.training.modulith.shared.api.order.OrderInternalApi {

  private final OrderRepo orderRepo;
  private final ShippingInternalApi shippingInternalApi;
  private final InventoryInternalApi inventoryInternalApi;

//  public void confirmPaymentForOrder(long orderId, boolean ok, PaymentGatewayWebHookApi paymentGatewayWebHookApi) {
//  @EventListener
@ApplicationModuleListener
@Override
public void confirmPaymentForOrder(PaymentCompletedEvent event) {
    long orderId = event.orderId();
    boolean ok = event.ok();
    Order order = orderRepo.findById(orderId).orElseThrow();
    order.pay(ok);
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      inventoryInternalApi.confirmReservation(order.id());
      String trackingNumber = shippingInternalApi.requestShipment(order.id(), order.shippingAddress());
      order.wasScheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
