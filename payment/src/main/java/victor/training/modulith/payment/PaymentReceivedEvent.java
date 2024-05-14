package victor.training.modulith.payment;

public record PaymentReceivedEvent(
    long orderId,
    boolean ok) {
}
