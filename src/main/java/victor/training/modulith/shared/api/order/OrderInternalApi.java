package victor.training.modulith.shared.api.order;

import org.springframework.modulith.ApplicationModuleListener;
import victor.training.modulith.shared.api.payment.PaymentCompletedEvent;

public interface OrderInternalApi {
  //  public void confirmPaymentForOrder(long orderId, boolean ok, PaymentGatewayWebHookApi paymentGatewayWebHookApi) {
  //  @EventListener
  @ApplicationModuleListener
  // stores the event in DB and reads it back in another thread
  void confirmPaymentForOrder(PaymentCompletedEvent event);
}
