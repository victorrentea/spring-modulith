package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.service.ReserveStockService;
import victor.training.modulith.order.InventoryModuleInterface;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryModuleApi implements InventoryModuleInterface {
  private final ReserveStockService reserveStockService;
  private final StockRepo stockRepo;

  public void reserveStock(long orderId, List<LineItem> items) {
    reserveStockService.reserveStock(orderId, items);
  }

  public int getStock(long productId) {
    Stock stock = stockRepo.findByProductId(productId).orElseThrow();
    return stock.items();
  }


}
