package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.impl.*;
import victor.training.modulith.order.InventoryModuleInterface;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryModule implements InventoryModuleInterface {
  private final ReserveStockService reserveStockService;
  private final StockRepo stockRepo;

  public void reserveStock(long orderId, List<LineItem> items) {
    reserveStockService.reserveStock(orderId, items);
  }

  public int getAvailableStock(long productId) {
    return stockRepo.findByProductId(productId).get().items();
  }
}
