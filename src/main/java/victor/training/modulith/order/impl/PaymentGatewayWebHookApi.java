package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.shipping.ShippingInternalApi;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = HTTP call back to me from a third party
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final OrderRepo orderRepo;
  private final ShippingInternalApi shippingInternalApi;
  private final InventoryInternalApi inventoryInternalApi;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
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
