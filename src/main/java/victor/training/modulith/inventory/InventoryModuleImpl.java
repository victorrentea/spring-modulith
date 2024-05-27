package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.service.ReserveStockService;
import victor.training.modulith.shared.LineItem;
import victor.training.modulith.shared.api.order.InventoryModuleInterface;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryModuleImpl implements InventoryModuleInterface, victor.training.modulith.shared.api.inventory.InventoryModuleApi {
  private final ReserveStockService reserveStockService;
  private final StockRepo stockRepo;

  @Override
  public void reserveStock(long orderId, List<LineItem> items) {
    reserveStockService.reserveStock(orderId, items);
  }

  @Override
  public int getStock(long productId) {
    Stock stock = stockRepo.findByProductId(productId).orElseThrow();
    return stock.items();
  }


}
