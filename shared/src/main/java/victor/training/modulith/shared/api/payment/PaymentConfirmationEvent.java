package victor.training.modulith.shared.api.payment;

public record PaymentConfirmationEvent(
    long orderId, boolean ok) {
}
