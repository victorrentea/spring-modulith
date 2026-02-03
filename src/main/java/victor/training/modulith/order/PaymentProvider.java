package victor.training.modulith.order;

public interface PaymentProvider {
  String generatePaymentUrl(long orderId, double total);
}
// with more implementations tomorrow Stripe, PayPal, .... bitCoin...
