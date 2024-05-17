package victor.training.modulith.shared.api.payment;

public record PaymentCompletedEvent(Long orderId, boolean ok) {
}
