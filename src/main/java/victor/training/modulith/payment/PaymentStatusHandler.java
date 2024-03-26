package victor.training.modulith.payment;

public interface PaymentStatusHandler {
  void processPayment(long orderId, boolean ok);
}
