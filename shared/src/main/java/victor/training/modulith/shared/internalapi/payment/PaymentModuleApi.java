package victor.training.modulith.shared.internalapi.payment;

public interface PaymentModuleApi {
  String generatePaymentUrl(long orderId, double total);
}
