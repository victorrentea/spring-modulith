package victor.training.modulith.order;

// SPI others to implement
public interface PaymentUrlGenerator {
  String generatePaymentUrl(long orderId, double total);
}
