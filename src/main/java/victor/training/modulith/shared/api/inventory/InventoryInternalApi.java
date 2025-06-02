package victor.training.modulith.shared.api.inventory;

public interface InventoryInternalApi {
  void reserveStock(StockReservationRequestIdo reservationRequest);
  void confirmReservation(long orderId);
  void cancelReservation(Long orderId);
  int getStock(long productId);
}
