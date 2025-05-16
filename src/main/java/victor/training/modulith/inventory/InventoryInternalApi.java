package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.service.StockService;
import victor.training.modulith.shared.LineItem;

import java.util.List;

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

  //a) it's my module -> change it
  //module managed by another team->
  //b) raise a ticket and block your dev until those son on the beaches do it - waste
  //c) open-source model: I change their code and submit them a dedicated PR that they have to 4-eyes review.⭐️!important
      // via CODEOWNERS file
  public int getStock(long productId) {
    return stockRepo.findById(productId).map(Stock::items).orElse(0);
  }
}
