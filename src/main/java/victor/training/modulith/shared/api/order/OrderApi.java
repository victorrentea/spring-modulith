package victor.training.modulith.shared.api.order;

public interface OrderApi {
  void confirmPayment(long orderId, boolean ok);
}
