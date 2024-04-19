package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.shipping.in.api.ShippingModuleApi;

@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final OrderRepo orderRepo;
  private final ShippingModuleApi shippingModule;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    Order order = orderRepo.findById(orderId).orElseThrow();
    order.paid(ok);
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
    System.out.println("Exit");
    return "Payment callback received";
  }
}
