package victor.training.modulith.order;

public record OrderStatusChangedEvent(
    Long orderId,
    OrderStatus status,
    String customerId
) {
}
