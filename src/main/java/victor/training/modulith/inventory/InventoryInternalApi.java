package victor.training.modulith.inventory;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.repo.StockReservationRepo;
import victor.training.modulith.inventory.service.StockService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
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

  public Optional<Integer> getStock(Long productId) {
    return stockRepo.findByProductId(productId)
        .map(Stock::items);

//        - stockReservationRepo.getStockReservationsByProductId(productId)
//        .stream().mapToInt(StockReservation::items).sum()

         // YOLO code💩 written by a feature team mercenary
    // that didn't grow up here
  }
}
