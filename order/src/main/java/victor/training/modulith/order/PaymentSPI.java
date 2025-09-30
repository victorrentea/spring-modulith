package victor.training.modulith.order;

public interface PaymentSPI {
  // Cycle fix #2: dep inversion using an interface
  String generatePaymentUrl(long orderId, double total);
}
