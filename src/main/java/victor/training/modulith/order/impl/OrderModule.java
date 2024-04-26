package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.api.order.OrderStatus;
import victor.training.modulith.shared.api.shipping.ShippingModuleApi;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderModule implements victor.training.modulith.shared.api.order.OrderModuleApi {
  private final OrderRepo orderRepo;
  private final ShippingModuleApi shippingModule;

  @Override
  public void confirmOrderPayment(long orderId, boolean ok) {
    Order order = orderRepo.findById(orderId).orElseThrow();
    order.paid(ok);
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
