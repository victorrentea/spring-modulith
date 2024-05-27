package victor.training.modulith.paymentapi;

public interface PaymentModuleApi {
  String generatePaymentUrl(long orderId, double total);
}
