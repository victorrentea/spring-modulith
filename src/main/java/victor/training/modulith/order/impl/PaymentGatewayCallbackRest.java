package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.NotificationForOrderService;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.shipping.ShippingModule;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PaymentGatewayCallbackRest {
  private final OrderRepo orderRepo;
  private final ShippingModule shippingModule;
  private final NotificationForOrderService notificationService;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    Order order = orderRepo.findById(orderId).orElseThrow();
    order.paid(ok);
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
      notificationService.sendOrderShippedEmail(order.id(), order.customerId());
    }
    return "Payment callback received";
  }

}
