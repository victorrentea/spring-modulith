package victor.training.modulith.payment;

public record OrderPaidEvent(long orderId, boolean ok) {
}
