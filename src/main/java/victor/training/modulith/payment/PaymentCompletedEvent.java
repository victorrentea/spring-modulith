package victor.training.modulith.payment;

public record PaymentCompletedEvent(
    long orderId, // imperfect. we should abstract a paymentId
    boolean ok
) {
}
