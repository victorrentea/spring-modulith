package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.shared.OrderModuleInternalApi;
import victor.training.modulith.shipping.in.api.ShippingModule;

@Service
@RequiredArgsConstructor
public class OrderModule implements OrderModuleInternalApi {
  private final OrderRepo orderRepo;
  private final ShippingModule shippingModule;

  @Override
  public void updatePaymentStatus(long orderId, boolean ok) {
    Order order = orderRepo.findById(orderId).orElseThrow();
    order.paid(ok);
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
