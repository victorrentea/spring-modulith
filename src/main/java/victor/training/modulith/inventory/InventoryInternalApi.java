package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.model.StockReservation;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.repo.StockReservationRepo;
import victor.training.modulith.inventory.service.StockService;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryInternalApi {
  private final StockService stockService;
  private final StockRepo stockRepo;
  private final StockReservationRepo stockReservationRepo;

  public void reserveStock(StockReservationRequestKnob reservationRequest) {
    stockService.reserveStock(reservationRequest.orderId(), reservationRequest.items());
  }

  public void confirmReservation(long orderId) {
    stockService.confirmReservation(orderId);
  }

  public int getStock(long productId) {
    int reservedItems = stockReservationRepo.findByProductId(productId)
        .stream()
        .mapToInt(StockReservation::items)
        .sum();
    return stockRepo.findByProductId(productId)
        .map(Stock::items)
        .orElse(0)
        -reservedItems; // OpenSource model: catalog sumbits a PR to inventory team, 2 people from inventory have to review this.
  }
}
