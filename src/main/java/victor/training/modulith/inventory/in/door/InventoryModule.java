package victor.training.modulith.inventory.in.door;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.app.entity.Stock;
import victor.training.modulith.inventory.app.repo.StockRepo;
import victor.training.modulith.inventory.app.service.ReserveStockService;
import victor.training.modulith.inventory.app.service.StockService;
import victor.training.modulith.order.InventoryModuleApi;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryModule implements InventoryModuleApi {
  private final ReserveStockService reserveStockService;
  private final StockRepo stockRepo;

  public void reserveStock(long orderId, List<LineItem> items) {
    reserveStockService.reserveStock(orderId, items);
  }

  public int getStock(long productId) {
    return stockRepo.findById(productId).map(Stock::items).orElseThrow();
  }
}
