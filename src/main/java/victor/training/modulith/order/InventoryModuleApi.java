package victor.training.modulith.order;

import victor.training.modulith.shared.LineItem;

import java.util.List;

public interface InventoryModuleApi { // Dependency Inversion o solve a cycle // TODO explore
   void reserveStock(long orderId, List<LineItem> items);
}
