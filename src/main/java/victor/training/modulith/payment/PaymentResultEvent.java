package victor.training.modulith.payment;

public record PaymentResultEvent(long orderId, boolean ok) {
}
