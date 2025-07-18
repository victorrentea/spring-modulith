package victor.training.modulith.shared.api.inventory;

public interface InventoryInternalApi {
  void reserveStock(StockReservationRequestIDto reservationRequest);
  void confirmReservation(long orderId);
  void cancelReservation(Long orderId);
  // consumer-driven contract
  int getStock(long productId);
}
