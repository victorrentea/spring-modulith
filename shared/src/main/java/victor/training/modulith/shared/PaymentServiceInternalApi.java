package victor.training.modulith.shared;

public interface PaymentServiceInternalApi {
  String generatePaymentUrl(long orderId, double total);
}
