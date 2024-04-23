package victor.training.modulith.payment.impl;

public interface OrderModuleForPayment {
  void onPaymentDone(long orderId, boolean ok);
}
