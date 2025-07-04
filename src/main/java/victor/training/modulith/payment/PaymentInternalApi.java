package victor.training.modulith.payment;

public interface PaymentInternalApi {
  String generatePaymentUrl(long orderId, double total);
}
