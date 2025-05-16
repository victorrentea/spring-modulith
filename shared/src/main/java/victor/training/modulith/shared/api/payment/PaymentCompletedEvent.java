package victor.training.modulith.shared.api.payment;

public record PaymentCompletedEvent(long orderId, boolean ok) {
}
