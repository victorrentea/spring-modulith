package victor.training.modulith.shared.api.payment;

public interface PaymentInternalApi {
  String generatePaymentUrl(long orderId, double total);
}
