package victor.training.modulith.shipping;

public interface ShippingInternalApi {
  String requestShipment(long orderId, String customerAddress);
}
