package victor.training.modulith.inventory;

import victor.training.modulith.shared.LineItem;

import java.util.List;

public interface InventoryInternalApi {
  void reserveStock(long orderId, List<LineItem> items);

  void confirmReservation(long orderId);

  int getStockForProduct(long productId);
}
