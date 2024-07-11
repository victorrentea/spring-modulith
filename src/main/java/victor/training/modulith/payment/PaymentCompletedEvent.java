package victor.training.modulith.payment;

public record PaymentCompletedEvent(
    Long orderId,
    boolean ok) {
}
