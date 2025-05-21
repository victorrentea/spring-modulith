package victor.training.modulith.shared.api.payment;

public record PaymentCompletedEvent(
    long orderId, // imperfect. we should abstract a paymentId
    boolean ok
) {
}
