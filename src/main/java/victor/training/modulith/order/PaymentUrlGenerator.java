package victor.training.modulith.order;

public interface PaymentUrlGenerator {
  String generatePaymentUrl(long orderId, double total);
  record PaymentUrl(String url) {
  }
}
