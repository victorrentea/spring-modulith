package victor.training.modulith.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.model.StockReservation;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.repo.StockReservationRepo;
import victor.training.modulith.shared.api.inventory.InventoryInternalApi;
import victor.training.modulith.shared.api.inventory.StockReservationRequestIDto;

import java.util.Optional;

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

  @Override
  public Optional<Integer> findStockByProductId(long productId) {
    Optional<Stock> stock = stockRepo.findByProductId(productId);
    if (stock.isEmpty()) {
      return Optional.empty();
    }
    int reservedItems = stockReservationRepo.getStockReservationsByProductId(productId)
        .stream()
        .mapToInt(StockReservation::items).sum();
    return Optional.of(stock.get().items() /*- reservedItems*/);
  }
}

//A) Raise a request to team-blue
// - delays
//B) Open-source-model: green-team: do your best, and 2 devs of blue-team review the chagen
// - bug risks (the more complex the logic)
// - review is 6 months late