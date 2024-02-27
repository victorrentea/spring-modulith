package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.OrderModule;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.repo.OrderRepo;
import victor.training.modulith.shipping.in.api.ShippingModule;

@RestController
@RequiredArgsConstructor
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final OrderModule orderModule;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    orderModule.confirmPayment(orderId, ok);
    return "Payment callback received";
  }
}
