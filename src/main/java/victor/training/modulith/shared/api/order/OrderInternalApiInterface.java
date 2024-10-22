package victor.training.modulith.shared.api.order;

public interface OrderInternalApiInterface {
  void confirmPayment(long orderId, boolean ok);
}
