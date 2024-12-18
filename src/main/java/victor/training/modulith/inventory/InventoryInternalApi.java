package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.service.StockService;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Service // from other modules
@RequiredArgsConstructor
public class InventoryInternalApi {
  private final StockService stockService;

  public void reserveStock(long orderId, List<LineItem> items) {
    stockService.reserveStock(orderId, items);
  }

  public void confirmReservation(long orderId) {
    stockService.confirmReservation(orderId);
  }

  //  @Secured("ROLE_ADMIN")
  public int getStock(long productId) {
    return stockService.getStock(productId);
  }
}
