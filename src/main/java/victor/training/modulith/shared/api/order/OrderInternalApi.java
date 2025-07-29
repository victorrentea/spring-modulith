package victor.training.modulith.shared.api.order;

public interface OrderInternalApi {
  void confirmOrderPayment(long orderId, boolean ok);
}
