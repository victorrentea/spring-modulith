package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.StockReservation;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.repo.StockReservationRepo;
import victor.training.modulith.inventory.service.StockService;

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

  public int getStockByProduct(Long productId) {
    // Option1: DIY and then request 2+ reviews from people caring for this module (incl 1 Elder=SME=Owner)
    //   -you can do mistakes in their domain
    // Option2: Raise them a PR! and wait 6 months for them to start on it, 9 mo to finish🤣;
    //   -you block
    int reservedItems = stockReservationRepo.getStockReservationsByProductId(productId).stream()
        .mapToInt(StockReservation::items)
        .sum();
    return stockRepo.findByProductId(productId).orElseThrow().items() /*- reservedItems*/;
  }
}
