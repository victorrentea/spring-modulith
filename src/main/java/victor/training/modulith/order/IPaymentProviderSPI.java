package victor.training.modulith.order;

public interface IPaymentProviderSPI {
  String generatePaymentUrl(long orderId, double total);
}
