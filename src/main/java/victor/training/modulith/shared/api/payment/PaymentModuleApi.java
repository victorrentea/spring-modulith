package victor.training.modulith.shared.api.payment;

public interface PaymentModuleApi {
  String generatePaymentUrl(long orderId, double total);
}
