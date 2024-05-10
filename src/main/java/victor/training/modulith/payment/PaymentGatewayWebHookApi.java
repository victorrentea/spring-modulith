package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.shipping.IOrderService;

@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi {
  private final IOrderService orderService; // TODO move to 'payment' module
//  private final OrderService orderService;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    orderService.onOrderPaid(orderId, ok);
//    eventPublisher.publishEvent(new OrderPaidEvent(orderId, ok));
    return "Payment callback received";
  }


}
