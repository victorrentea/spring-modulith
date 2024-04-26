package victor.training.modulith.shared.api.order;

public interface PaymentUrlProvider {
  String generatePaymentUrl(long orderId, double total);
}
