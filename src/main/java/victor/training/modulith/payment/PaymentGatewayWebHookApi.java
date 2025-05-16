package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.OrderInternalApi;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.shipping.ShippingInternalApi;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = a call back to me over HTTP
public class PaymentGatewayWebHookApi { // TODO move to 'payment' module
  private final ApplicationEventPublisher applicationEventPublisher;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
    applicationEventPublisher.publishEvent(new PaymentCompletedEvent(orderId, ok));
    return "Payment callback received";
  }
  // avoid events because:
  // harder to navigate/stack trace
  // most people assume listeners run in a separate threead = FALSE by default
  // applicationEventPublisher + @EventListener are in--mem event bus= GOOD === strictly consistent and sync
  //   => listeners BLOCK the publisher thread
  //   => listeners exceptions fire up in publisher
  //   => listeners can rollback a transaction in PUBLISHER
  // hard to maintain.
  // don't use in-mem events unless you plan to eject one of the two modules to a separate microservice
  // in this Quarter!!!
  // use such events as a sandbox (staging) to crystalize future service-bus/kafka events

  // MORE ROBUST: store it and poll it

}
