package victor.training.modulith.order;

public interface PaymentSPI {
  String generatePaymentUrl(long orderId, double total);
}
