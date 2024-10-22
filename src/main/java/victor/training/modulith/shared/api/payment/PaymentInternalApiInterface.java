package victor.training.modulith.shared.api.payment;

public interface PaymentInternalApiInterface {
  String generatePaymentUrl(long orderId, double total);
}
