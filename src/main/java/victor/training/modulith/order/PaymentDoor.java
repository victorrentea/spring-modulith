package victor.training.modulith.order;

public interface PaymentDoor {
  String generatePaymentUrl(long orderId, double total);
}
