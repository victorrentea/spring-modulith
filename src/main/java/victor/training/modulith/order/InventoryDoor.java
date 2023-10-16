package victor.training.modulith.order;

import victor.training.modulith.common.LineItem;

import java.util.List;

public interface InventoryDoor {
   void reserveStock(long orderId, List<LineItem> items);
}
