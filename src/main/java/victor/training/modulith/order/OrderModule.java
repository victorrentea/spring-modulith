package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.shipping.ShippingModule;

@Component
@RequiredArgsConstructor
public class OrderModule {
  private final OrderRepo orderRepo;
  private final ShippingModule shippingModule;
  public void setOrderPaid(long orderId, boolean ok) {
    Order order = orderRepo.findById(orderId).orElseThrow();
    order.paid(ok);
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
