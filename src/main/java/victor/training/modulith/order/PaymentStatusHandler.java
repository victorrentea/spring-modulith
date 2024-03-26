package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.shipping.in.api.ShippingModule;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentStatusHandler {
  private final OrderRepo orderRepo;
  private final ShippingModule shippingModule;

  public void processPayment(long orderId, boolean ok) {
    Order order = orderRepo.findById(orderId).orElseThrow();
    order.paid(ok);
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
