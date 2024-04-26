package victor.training.modulith.shared.api.shipping;

public interface ShippingModuleApi {
  String requestShipment(long orderId, String customerAddress);
}
