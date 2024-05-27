package victor.training.modulith.order;

public interface PaymentModuleInterface {
  String generatePaymentUrl(long orderId, double total);
}
