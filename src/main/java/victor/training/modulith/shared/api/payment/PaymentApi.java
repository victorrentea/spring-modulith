package victor.training.modulith.shared.api.payment;

public interface PaymentApi {
  String generatePaymentUrl(long orderId, double total);
}
