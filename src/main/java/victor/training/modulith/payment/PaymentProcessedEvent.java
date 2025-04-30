package victor.training.modulith.payment;

public record PaymentProcessedEvent(
    long orderId, boolean ok
) {
}
