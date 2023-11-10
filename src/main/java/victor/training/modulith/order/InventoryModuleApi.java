package victor.training.modulith.order;

import victor.training.modulith.shared.LineItem;

import java.util.List;

public interface InventoryModuleApi {
   void reserveStock(long orderId, List<LineItem> items);
}
