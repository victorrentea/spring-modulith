package victor.training.modulith.shared.api.inventory;

public interface InventoryInternalApi {
  void reserveStock(StockReservationRequestIDto reservationRequest);
  void confirmReservation(long orderId);
  void cancelReservation(Long orderId);
  int getStockForProduct(long productId);
}
