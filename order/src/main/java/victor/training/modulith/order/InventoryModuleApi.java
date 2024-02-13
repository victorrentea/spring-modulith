package victor.training.modulith.order;

import victor.training.modulith.shared.LineItem;

import java.util.List;

// Dependency Inversion solves a cycle // TODO explore
public interface InventoryModuleApi {
   void reserveStock(long orderId, List<LineItem> items);
}
