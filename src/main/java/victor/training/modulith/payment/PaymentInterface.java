package victor.training.modulith.payment;

public interface PaymentInterface {
  void processPayment(long orderId, boolean ok);
}
