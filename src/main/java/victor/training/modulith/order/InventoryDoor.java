package victor.training.modulith.order;

import victor.training.modulith.shared.LineItem;
import victor.training.modulith.shared.ProductId;

import java.util.List;
import java.util.Map;

public interface InventoryDoor {
   void reserveStock(long orderId, List<LineItem> items);
}
