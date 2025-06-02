package victor.training.modulith.shared.api.order;

public interface OrderInternalApi {
  void confirmPayment(long orderId, boolean ok);
}
