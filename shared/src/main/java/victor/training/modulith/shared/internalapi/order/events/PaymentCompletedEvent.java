package victor.training.modulith.shared.internalapi.order.events;

public record PaymentCompletedEvent(
    long orderId, boolean ok) {
}
