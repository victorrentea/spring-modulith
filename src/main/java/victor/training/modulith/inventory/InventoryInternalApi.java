package victor.training.modulith.inventory;

public interface InventoryInternalApi {
  void reserveStock(StockReservationRequestIDto reservationRequest);
  void confirmReservation(long orderId);
  void cancelReservation(Long orderId);
  int getStockForProduct(long productId);
}
