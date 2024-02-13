package victor.training.modulith.payment;

public interface PaymentStatusListener { // dependency inversion

  void paymentConfirmed(long orderId, boolean ok);
  // dependency inversion allows the other
  // module you call to return you results,
  // unlike events
}
