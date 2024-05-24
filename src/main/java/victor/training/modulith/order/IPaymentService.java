package victor.training.modulith.order;

public interface IPaymentService {
  String generatePaymentUrl(long orderId, double total);
}
