package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.service.StockService;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryInternalApiImpl implements InventoryInternalApi {
  private final StockService stockService;
  private final StockRepo stockRepo;

  @Override
  public void reserveStock(long orderId, List<LineItem> items) {
//    Product
    stockService.reserveStock(orderId, items);
  }

  @Override
  public void confirmReservation(long orderId) {
    stockService.confirmReservation(orderId);
  }

  @Override
  public int getStockForProduct(long productId) {
    return stockRepo.findByProductId(productId).orElseThrow().items();
  }

}
