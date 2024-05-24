package victor.training.modulith.payment;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.order.OrderModuleApi;

@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final ApplicationEventPublisher eventPublisher;

  @PutMapping("payment/{orderId}/status")
  @Transactional
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
//    orderModuleApi.onPaymentResult(orderId, ok);
    eventPublisher.publishEvent(new PaymentCompletedEvent(orderId, ok)); // exact aici pe linia asta
    // TOTI listenerii de eventuri vor fi executati sincron in threadul si tranzactia mea

    System.out.println("Exit");
    return "Payment callback received";
  }


}
