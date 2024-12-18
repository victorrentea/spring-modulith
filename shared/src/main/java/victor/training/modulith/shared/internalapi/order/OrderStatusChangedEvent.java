package victor.training.modulith.shared.internalapi.order;

public record OrderStatusChangedEvent(
    Long orderId,
    OrderStatus status,
    String customerId
) {
}
