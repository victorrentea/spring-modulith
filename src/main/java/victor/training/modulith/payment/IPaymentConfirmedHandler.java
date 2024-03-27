package victor.training.modulith.payment;

public interface IPaymentConfirmedHandler {
  void confirmPayment(long orderId, boolean ok);
}
