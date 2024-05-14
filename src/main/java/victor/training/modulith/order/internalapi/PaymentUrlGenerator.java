package victor.training.modulith.order.internalapi;

public interface PaymentUrlGenerator {
  String generatePaymentUrl(long orderId, double total);
}
