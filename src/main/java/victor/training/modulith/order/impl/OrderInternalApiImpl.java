package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.shipping.ShippingInternalApi;

@Service
@RequiredArgsConstructor
public class OrderInternalApiImpl implements victor.training.modulith.order.OrderInternalApi {
  private final OrderRepo orderRepo;
  private final InventoryInternalApi inventoryInternalApi;
  private final ShippingInternalApi shippingInternalApi;

  @Override
  public void confirm(long orderId, boolean ok) {
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
