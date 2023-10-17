package victor.training.modulith.order;

public record OrderStatusChangedEvent(
    long orderId, OrderStatus status, String customerId) {
}
