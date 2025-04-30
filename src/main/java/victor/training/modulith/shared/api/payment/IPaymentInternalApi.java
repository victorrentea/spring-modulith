package victor.training.modulith.shared.api.payment;

public interface IPaymentInternalApi {
  String generatePaymentUrl(long orderId, double total);
}
