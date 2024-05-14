package victor.training.modulith.order.internalapi;

public record OrderStatusChangedEvent(
    Long orderId,
    OrderStatus status,
    String customerId
) {
}
