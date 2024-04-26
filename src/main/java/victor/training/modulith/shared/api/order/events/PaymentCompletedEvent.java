package victor.training.modulith.shared.api.order.events;

public record PaymentCompletedEvent(
    long orderId, boolean ok) {
}
