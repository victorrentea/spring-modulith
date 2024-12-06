package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.OrderInternalApi;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi {
  private final OrderInternalApi orderInternalApi; // TODO move to 'payment' module

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    orderInternalApi.confirmPayment(orderId, ok);
    return "Payment callback received";
  }
}
