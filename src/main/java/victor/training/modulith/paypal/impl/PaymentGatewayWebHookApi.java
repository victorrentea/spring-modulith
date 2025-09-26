package victor.training.modulith.paypal.impl;

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
// Webhook = HTTP call back to me from a third party
public class PaymentGatewayWebHookApi {
  private final OrderInternalApi orderInternalApi; // TODO move to 'payment' module

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    orderInternalApi.confirm(orderId, ok);
    // to break this dep cycle:
    // Fix #1) TODO eventPublisher.publishEvent(new PaymentCompleted()) + @EventListener in order
    // Fix #2) Payment implements the order-module's SPI
    // Fix #3) TODO orchestrate from a 'higher-level' aggregator
    return "Payment callback received";
  }


}
