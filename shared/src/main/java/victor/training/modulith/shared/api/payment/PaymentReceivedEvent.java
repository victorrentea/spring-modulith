package victor.training.modulith.shared.api.payment;

public record PaymentReceivedEvent(
    long orderId,
    boolean ok) {
}
