package victor.training.modulith.orderapi;

public record OrderStatusChangedEvent(
    Long orderId,
    OrderStatus status,
    String customerId
) {
}
