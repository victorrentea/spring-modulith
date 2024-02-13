package victor.training.modulith.payment;

public interface PaymentConfirmedHook { // dependency inversion

  void paymentConfirmed(long orderId, boolean ok);
  // this allows the other module you call to return you results, unlike events
}
