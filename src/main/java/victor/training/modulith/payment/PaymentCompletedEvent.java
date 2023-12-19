package victor.training.modulith.payment;

public record PaymentCompletedEvent(long paymentId, boolean ok) {
}
