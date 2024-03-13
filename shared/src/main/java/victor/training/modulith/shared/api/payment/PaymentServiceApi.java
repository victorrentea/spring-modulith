package victor.training.modulith.shared.api.payment;

public interface PaymentServiceApi {
  String generatePaymentUrl(long orderId, double total);
}
