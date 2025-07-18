package victor.training.modulith.order;

public interface PaymentUrlGeneratorPort {
  String generatePaymentUrl(long orderId, double total);
}
