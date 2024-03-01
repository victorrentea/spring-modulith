package victor.training.modulith.payment;

public record PaymentProcessedEvent(Long orderId, boolean ok) {
}
