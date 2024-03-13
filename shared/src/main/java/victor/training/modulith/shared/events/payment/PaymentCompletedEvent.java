package victor.training.modulith.shared.events.payment;

public record PaymentCompletedEvent(long orderId, boolean ok) {
}
