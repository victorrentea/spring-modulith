package victor.training.modulith.orderapi;

import victor.training.modulith.shared.LineItem;

import java.util.List;

// Dependency Inversion was used here to solve a dependency cycle
public interface InventoryModuleInterface {
   void reserveStock(long orderId, List<LineItem> items);
}
