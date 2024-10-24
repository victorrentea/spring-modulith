package victor.training.modulith.shared;

public interface PaymentInternalApi {
  String generatePaymentUrl(long orderId, double total);
}
