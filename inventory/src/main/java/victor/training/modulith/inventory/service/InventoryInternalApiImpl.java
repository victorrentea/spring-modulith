package victor.training.modulith.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.repo.StockReservationRepo;
import victor.training.modulith.shared.api.inventory.StockReservationRequestIdo;

@Service
@RequiredArgsConstructor
public class InventoryInternalApiImpl implements victor.training.modulith.shared.api.inventory.InventoryInternalApi {
  private final StockService stockService;
  private final StockRepo stockRepo;
  private final StockReservationRepo stockReservationRepo;

  @Override
  public void reserveStock(StockReservationRequestIdo reservationRequest) {
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
  public int getStock(long productId) {
    return stockRepo.findByProductId(productId).orElseThrow().items();
  }
}
