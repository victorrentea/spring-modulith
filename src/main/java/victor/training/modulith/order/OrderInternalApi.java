package victor.training.modulith.order;

public interface OrderInternalApi {
  void confirm(long orderId, boolean ok);
}
