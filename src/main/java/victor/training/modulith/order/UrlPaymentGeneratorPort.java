package victor.training.modulith.order;

public interface UrlPaymentGeneratorPort {
  String generatePaymentUrl(long orderId, double total);
}
