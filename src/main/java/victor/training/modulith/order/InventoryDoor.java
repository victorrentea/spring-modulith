package victor.training.modulith.order;

import victor.training.modulith.shared.ProductId;

import java.util.Map;

public interface InventoryDoor {
   void reserveStock(long orderId, Map<ProductId, Integer> items);
}
