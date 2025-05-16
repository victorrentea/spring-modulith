package victor.training.modulith.order;

// move to shared when another module needs payment
public interface PaymentUrlGenerator {
  String generatePaymentUrl(long orderId, double total);
}
