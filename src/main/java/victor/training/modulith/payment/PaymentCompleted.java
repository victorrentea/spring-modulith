package victor.training.modulith.payment;

public record PaymentCompleted(
    long orderId,
    boolean ok
) {
}
