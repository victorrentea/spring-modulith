package victor.training.modulith.order;

import victor.training.modulith.common.LineItem;

import java.util.List;

public interface InventoryModuleApi {
   void reserveStock(long orderId, List<LineItem> items);
}
