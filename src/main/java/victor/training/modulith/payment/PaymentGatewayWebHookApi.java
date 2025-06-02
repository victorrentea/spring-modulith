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
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.shipping.ShippingInternalApi;

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
    return "Payment callback received";
  }
}
