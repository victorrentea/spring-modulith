package victor.training.modulith.order;

public interface IOrderPaymentHandler {
  void onPaymentCompleted(long orderId, boolean ok);
}
