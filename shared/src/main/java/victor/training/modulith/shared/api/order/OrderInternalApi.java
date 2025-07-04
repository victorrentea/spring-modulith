package victor.training.modulith.shared.api.order;

public interface OrderInternalApi {
  void confirm(long orderId, boolean ok);
}
