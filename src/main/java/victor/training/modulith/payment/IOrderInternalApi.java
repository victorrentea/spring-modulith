package victor.training.modulith.payment;

public interface IOrderInternalApi {
  void confirmPayment(long orderId, boolean ok);
}
