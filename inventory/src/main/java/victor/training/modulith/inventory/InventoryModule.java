package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.impl.ReserveStockService;
import victor.training.modulith.inventory.impl.StockRepo;
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

  @Override
  public int getStockByProduct(long productId) {
    Integer stock = stockRepo.findByProductId(productId).orElseThrow().items();
    return stock;
  }

}
