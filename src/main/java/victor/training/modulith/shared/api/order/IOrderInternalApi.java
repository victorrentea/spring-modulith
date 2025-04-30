package victor.training.modulith.shared.api.order;

public interface IOrderInternalApi {
  void confirmPayment(long orderId, boolean ok);
}
