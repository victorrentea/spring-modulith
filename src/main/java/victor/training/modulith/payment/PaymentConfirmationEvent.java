package victor.training.modulith.payment;

public record PaymentConfirmationEvent(
    long orderId, boolean ok) {
}
