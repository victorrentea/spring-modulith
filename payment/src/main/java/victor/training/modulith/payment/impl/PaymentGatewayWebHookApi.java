package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.shared.api.payment.PaymentConfirmationEvent;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = HTTP call back to me from a third party
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final ApplicationEventPublisher applicationEventPublisher;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    // spring,Guice,Quarkus have in-memory event bus
    applicationEventPublisher.publishEvent(new PaymentConfirmationEvent(orderId, ok));
    // All devs assume the listeners run in another thread than publisher = WRONG by default.
    // - how to know if the listener succeeded?
    // - can these events get lost?

    // by default all the even listeners will run sequentially in the publishers thread,
    // blocking the publisher, throwing exceptions back to publisher if any fails and
    // sharing the transaction that might have been there

    // THEY MAKE CODE HARD TO NAVIGATE => AVOID
    // USE Only if you plan to eject one of the two modules as a microservice soon
    // as a staging ground until you switch to @KafkaListener..

    return "Payment callback received";
  }
  // order.PlaceOrderApi -> PaymentService.getPayUrl
  // order.cofirmPayment <- PaymentWebHook.call
  // ways to fix this cycle:
  // ✅ a) Dependency Inversion (via a new interface)
  // ✅ b) Event: payment->PaymentConfirmationEvent->order
  // X c) Orchestrate From Above: Facade module to call PlaceOrder, then getPayUrl
  // c-fullstack) fetch('placeorder').then(r=>fetch('paymenturl))
  // x d) 💖 Merge back the two modules; oups: wrong boundary
  // ✅✅ e) Segregate module contracts => no cycles are possible ⭐️
}
