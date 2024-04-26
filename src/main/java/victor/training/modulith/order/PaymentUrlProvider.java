package victor.training.modulith.order;

public interface PaymentUrlProvider {
  String generatePaymentUrl(long orderId, double total);
}
