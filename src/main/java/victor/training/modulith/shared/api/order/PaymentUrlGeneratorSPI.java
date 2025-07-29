package victor.training.modulith.shared.api.order;

public interface PaymentUrlGeneratorSPI {
  String generatePaymentUrl(long orderId, double total);
}
