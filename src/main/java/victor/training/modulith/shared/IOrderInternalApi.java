package victor.training.modulith.shared;

public interface IOrderInternalApi {
  void confirmPayment(long orderId, boolean ok);
}
