package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.shipping.ShippingInternalApi;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderService {
  private final OrderRepo orderRepo;
  private final InventoryInternalApi inventoryInternalApi;
  private final ShippingInternalApi shippingInternalApi;

  public String confirmPay(long orderId, boolean ok) {
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
    return "Payment callback received";
  }
}
