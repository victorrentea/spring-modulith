package victor.training.modulith.shared.api.order;

import org.springframework.modulith.ApplicationModuleListener;
import victor.training.modulith.shared.api.payment.PaymentConfirmationEvent;

public interface OrderInternalApi {
  //  public void confirmPayment(long orderId, boolean ok) {
  //  @EventListener
  @ApplicationModuleListener
  // ⭐️ async, DB-persisted events; different thread & transaction
  void onPaymentConfirmed(PaymentConfirmationEvent event);
}
