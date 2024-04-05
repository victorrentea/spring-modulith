package victor.training.modulith.shared;

public interface OrderModuleInternalApi {
  void updatePaymentStatus(long orderId, boolean ok);
}
