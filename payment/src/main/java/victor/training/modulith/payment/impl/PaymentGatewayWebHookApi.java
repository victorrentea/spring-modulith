package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.shared.api.order.OrderInternalApi;

@Slf4j
@RestController
@RequiredArgsConstructor
// Webhook = HTTP call back to me from a third party
public class PaymentGatewayWebHookApi {
  private final OrderInternalApi orderInternalApi; // TODO move to 'payment' module
//  private final ApplicationEventPublisher applicationEventPublisher;

  @PutMapping("payment/{orderId}/status")
  public String confirmPayment(@PathVariable long orderId, @RequestBody boolean ok) {
//    orderInternalApi.confirmPayment(orderId, ok);
    // a) event to shield 'payment' against 'order'
//    applicationEventPublisher.publishEvent(new PaymentReceived(orderId)); + PaymentFailed(orderId)
//    applicationEventPublisher.publishEvent(new PaymentCompletedEvent(orderId, ok));
    // events make code harder to navigate, expose you to framework magic = avoid unless:
    // use them to draft you future Solace/Kafka events you plan to extract one of the modules as a separate microservice

    // b) extract interface (Dependency Inversion in soliD):
    orderInternalApi.confirmPayment(orderId, ok);

    // c) merge back the two modules saying: "there is too much coupling between
    // 'pricing' and 'offers' to keep them separate"

    // d) "orchestrate from above"
    // d1) full-stack solution:
      //fetch(POST, /orders)
      //.then(r=>fetch(GET, /payment?r.orderId...))
    // d2) orchestrate from a separate module: "api", "site"
    // that calls the 'order' and 'payment' modules

    return "Payment callback received";
  }
}
