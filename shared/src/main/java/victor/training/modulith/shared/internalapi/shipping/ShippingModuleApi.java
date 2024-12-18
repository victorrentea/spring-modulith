package victor.training.modulith.shared.internalapi.shipping;

public interface ShippingModuleApi {
  String requestShipment(long orderId, String customerAddress);
}
