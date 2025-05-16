package victor.training.modulith.api.shared.shipping;

public interface ShippingInternalApi {
  String requestShipment(long orderId, String customerAddress);
}
