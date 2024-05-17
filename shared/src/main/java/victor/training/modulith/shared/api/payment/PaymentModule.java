package victor.training.modulith.shared.api.payment;

public interface PaymentModule {
  String generatePaymentUrl(long orderId, double total);
}
