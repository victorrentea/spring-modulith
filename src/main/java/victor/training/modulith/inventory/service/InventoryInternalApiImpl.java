package victor.training.modulith.inventory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.repo.StockReservationRepo;
import victor.training.modulith.shared.api.inventory.StockReservationRequestKnob;

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
  public int getStockByProduct(long productId) {
    // well, this is actually wrong and we need to subtract the existing reservations that are already for this product I
//    int reservedStock = stockReservationRepo.findByProductId(productId)
//        .stream().mapToInt(StockReservation::items).sum();


    return stockRepo.findByProductId(productId).map(Stock::items).orElse(0)
        /*-reservedStock*/;
  }
}
