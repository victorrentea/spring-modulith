package victor.training.modulith.payment.impl;

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
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final OrderInternalApi orderInternalApi;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    orderInternalApi.pay(orderId, ok);
    //Fix#1 cycle by firing an event called
    // - ❌PayOrder PR Rejected: it's a command,call ; not a FACT
    // - ✅PaymentConfirmedEvent(orderId, boolean ok)

    // ❌Polling: inefficient:
    // what if order module keeps asking payment Wazaaaa???
    // about my payment every 1 sec for all 10000 pending payments

    // ❌REST callback so there's no code dep anymore💪
    return "Payment callback received";
  }
}
