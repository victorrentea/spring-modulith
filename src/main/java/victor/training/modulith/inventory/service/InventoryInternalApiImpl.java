package victor.training.modulith.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.api.inventory.StockReservationRequestIDto;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.model.StockReservation;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.repo.StockReservationRepo;
import victor.training.modulith.shared.api.inventory.InventoryInternalApi;

@Service
@RequiredArgsConstructor
public class InventoryInternalApiImpl implements InventoryInternalApi {
  private final StockService stockService;
  private final StockRepo stockRepo;
  private final StockReservationRepo stockReservationRepo;

  @Override
  public void reserveStock(StockReservationRequestIDto reservationRequest) {
    stockService.reserveStock(reservationRequest.orderId(), reservationRequest.items());
  }

  @Override
  public void confirmReservation(long orderId) {
    stockService.confirmReservation(orderId);
  }

  @Override
  public void cancelReservation(Long orderId) {
    stockService.cancelReservation(orderId);
  }

  // consumer-driven contract
  @Override
  public int getStock(long productId) {
    Stock stock = stockRepo.findByProductId(productId).orElseThrow();
    int reservedItems = stockReservationRepo.getStockReservationsByProductId(productId).stream()
        .mapToInt(StockReservation::items)
        .sum();

    return stock.items() /*- reservedItems*/; // i think
  }
}
