package victor.training.modulith.shipping.out.event;

public record ShippingResultEvent(long orderId, boolean ok) {
}
