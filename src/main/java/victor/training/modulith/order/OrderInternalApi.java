package victor.training.modulith.order;

public interface OrderInternalApi {
  void confirmOrder(long orderId, boolean ok);
}
