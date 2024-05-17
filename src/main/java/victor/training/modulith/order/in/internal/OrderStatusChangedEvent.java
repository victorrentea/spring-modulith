package victor.training.modulith.order.in.internal;

public record OrderStatusChangedEvent(
    Long orderId,
    OrderStatus status,
    String customerId
) {
}
