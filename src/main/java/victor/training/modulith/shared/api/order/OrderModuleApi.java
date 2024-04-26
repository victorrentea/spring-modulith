package victor.training.modulith.shared.api.order;

public interface OrderModuleApi {
  void confirmOrderPayment(long orderId, boolean ok);
}
