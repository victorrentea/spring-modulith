package victor.training.modulith.order;

// Service Provider Interface: THEY implement MY contract
public interface PaymentUrlGeneratorSPI {
  String generatePaymentUrl(long orderId, double total);
}
