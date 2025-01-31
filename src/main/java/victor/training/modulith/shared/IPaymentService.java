package victor.training.modulith.shared;

public interface IPaymentService {
  String generatePaymentUrl(long orderId, double total);
}
