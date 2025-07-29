package victor.training.modulith.shared.api.inventory;

import java.util.Optional;

public interface InventoryInternalApi {
  void reserveStock(StockReservationRequestIDto reservationRequest);
  void confirmReservation(long orderId);
  void cancelReservation(Long orderId);
  Optional<Integer> findStockByProductId(long productId);
}
