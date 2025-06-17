package victor.training.modulith.shared.api.inventory;

public interface InventoryInternalApi {
  void reserveStock(StockReservationRequestKnob reservationRequest);
  void confirmReservation(long orderId);
  void cancelReservation(Long orderId);
  int getStockByProductId(Long productId);
}
