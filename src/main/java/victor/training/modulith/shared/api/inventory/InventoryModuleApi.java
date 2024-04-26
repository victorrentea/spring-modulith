package victor.training.modulith.shared.api.inventory;

import victor.training.modulith.shared.LineItem;

import java.util.List;

public interface InventoryModuleApi {
  void reserveStock(long orderId, List<LineItem> items);

  int getStock(long productId);
}
