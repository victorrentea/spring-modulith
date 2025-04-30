package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.shared.api.order.OrderStatus;
import victor.training.modulith.shipping.ShippingInternalApi;

@Service
@RequiredArgsConstructor
public class OrderInternalApi implements victor.training.modulith.shared.api.order.IOrderInternalApi {
  private final OrderRepo orderRepo;
  private final InventoryInternalApi inventoryInternalApi;
  private final ShippingInternalApi shippingInternalApi;

  @Override
  public void confirmPayment(long orderId, boolean ok) {
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
