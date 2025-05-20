package victor.training.modulith.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.api.inventory.StockReservationRequestKnob;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.model.StockReservation;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.repo.StockReservationRepo;

@Service
@RequiredArgsConstructor
public class InventoryInternalApiImpl implements victor.training.modulith.shared.api.inventory.InventoryInternalApi {
  private final StockService stockService;
  private final StockRepo stockRepo;
  private final StockReservationRepo stockReservationRepo;

  @Override
  public void reserveStock(StockReservationRequestKnob reservationRequest) {
    stockService.reserveStock(reservationRequest.orderId(), reservationRequest.items());
  }

  @Override
  public void confirmReservation(long orderId) {
    stockService.confirmReservation(orderId);
  }

  @Override
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
