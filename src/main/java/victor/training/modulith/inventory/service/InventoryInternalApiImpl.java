package victor.training.modulith.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.StockReservationRequestIDto;
import victor.training.modulith.inventory.repo.StockReservationRepo;

@Service
@RequiredArgsConstructor
public class InventoryInternalApiImpl implements victor.training.modulith.inventory.InventoryInternalApi {
  private final StockService stockService;
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
  public int getStockForProduct(long productId) {
    return stockService.getStockForProduct(productId);
  }
}
