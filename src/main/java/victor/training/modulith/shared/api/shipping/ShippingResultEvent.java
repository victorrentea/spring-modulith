package victor.training.modulith.shared.api.shipping;

public record ShippingResultEvent(long orderId, boolean ok) {
}
