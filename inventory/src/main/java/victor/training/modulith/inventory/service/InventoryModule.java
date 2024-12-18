package victor.training.modulith.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.shared.LineItem;
import victor.training.modulith.shared.internalapi.inventory.InventoryModuleApi;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryModule implements  InventoryModuleApi {
  private final ReserveStockService reserveStockService;
  private final StockRepo stockRepo;

  @Override
  public void reserveStock(long orderId, List<LineItem> items) {
    reserveStockService.reserveStock(orderId, items);
  }

  @Override
  public int getStock(long productId) {
    return stockRepo.findByProductId(productId)
        .map(Stock::items)
        .orElse(0);
  }
}
