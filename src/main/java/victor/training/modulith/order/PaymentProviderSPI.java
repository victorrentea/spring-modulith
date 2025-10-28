package victor.training.modulith.order;

public interface PaymentProviderSPI {
  String generatePaymentUrl(long orderId, double total);
}
