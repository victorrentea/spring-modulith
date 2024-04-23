package victor.training.modulith.payment;

public interface OrderModuleForPayment {
  void onPaymentDone(long orderId, boolean ok);
}
