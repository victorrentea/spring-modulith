package victor.training.modulith.inventory;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.repo.StockReservationRepo;
import victor.training.modulith.inventory.service.StockService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Observed
public class InventoryInternalApi {
  private final StockService stockService;
  private final StockRepo stockRepo;
  private final StockReservationRepo stockReservationRepo;

  public void reserveStock(StockReservationRequestIDto reservationRequest) {
    stockService.reserveStock(reservationRequest.orderId(), reservationRequest.items());
  }

  public void confirmReservation(long orderId) {
    stockService.confirmReservation(orderId);
  }

  public void cancelReservation(Long orderId) {
    stockService.cancelReservation(orderId);
  }

  public Optional<Integer> getStockByProduct(Long productId) {
    // naive impl written (guessed) by a dev not from here (TM)

    // if (b) =>
//    int reservedItems = stockReservationRepo.getStockReservationsByProductId(productId).stream().mapToInt(r -> r.items()).sum();
//    return stockRepo.findById(productId).orElseThrow().items() - reservedItems; // impl detail

    // Problems:
    // #1 impl decision: decrease master stock
    //  (a) when inserting the reservation
    //  (b) when reservation is confirmed

    // #2 some products have no stock associated at all, such as digital products like online games or vouchers or insurances.
    return stockRepo.findById(productId).map(Stock::items);
  }
}
