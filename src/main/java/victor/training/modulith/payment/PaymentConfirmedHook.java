package victor.training.modulith.payment;

public interface PaymentConfirmedHook {
  void paymentConfirmed(long orderId, boolean ok);
}
