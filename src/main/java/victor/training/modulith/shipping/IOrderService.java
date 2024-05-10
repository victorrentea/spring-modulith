package victor.training.modulith.shipping;

public interface IOrderService {
  void onOrderPaid(long orderId, boolean ok);
}
