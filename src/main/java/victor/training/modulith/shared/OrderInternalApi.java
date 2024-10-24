package victor.training.modulith.shared;

public interface OrderInternalApi {
  void confirmPaymentCompleted(long orderId, boolean ok);
}
