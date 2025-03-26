package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.service.StockService;
import victor.training.modulith.shared.LineItem;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryInternalApi {
  private final StockService stockService;
  private final StockRepo stockRepo;

  public void reserveStock(long orderId, List<LineItem> items) {
    stockService.reserveStock(orderId, items);
  }

  public void confirmReservation(long orderId) {
    stockService.confirmReservation(orderId);
  }

  public Optional<StockKnob> getStock(long productId) {
    return stockRepo.findByProductId(productId)
        .map(Stock::items)
        .map(stock -> new StockKnob(productId,stock));
  }

}
