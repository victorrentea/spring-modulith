package victor.training.modulith.shared.internalapi.shipping;

public record ShippingResultEvent(long orderId, boolean ok) {
}
