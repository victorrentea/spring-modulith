package victor.training.modulith.order;

public interface PaymentUrlGeneratorSPI {
  String generatePaymentUrl(long orderId, double total);
}
