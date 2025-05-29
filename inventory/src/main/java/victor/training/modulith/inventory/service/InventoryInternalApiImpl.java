package victor.training.modulith.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.api.inventory.StockReservationRequestKnob;
import victor.training.modulith.inventory.model.Stock;
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
  public void cancelReservation(Long orderId) {
    stockService.cancelReservation(orderId);
  }

  @Override
  public int getStockByProductId(long productId) {
    // This guy didn't know to substract all reservations from the total. (impl details) - invetory tech lead
//    int totalReserved = stockReservationRepo.findByProductId(productId).stream()
//        .mapToInt(StockReservation::items).sum();

    return stockRepo.findByProductId(productId).map(Stock::items).orElse(0)
//        - totalReserved
        ;


  }
}
