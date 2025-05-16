package victor.training.modulith.shared.api.shipping;

public interface ShippingInternalApi {
  String requestShipment(long orderId, String customerAddress);
}
