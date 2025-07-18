package victor.training.modulith.shared.api.order;

public interface OrderInternalApi {
  void confirmOrder(long orderId, boolean ok);
}
