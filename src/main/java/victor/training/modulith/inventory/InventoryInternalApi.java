package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.repo.StockReservationRepo;
import victor.training.modulith.inventory.service.StockService;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryInternalApi {
  private final StockService stockService;
  private final StockRepo stockRepo;
  private final StockReservationRepo stockReservationRepo;

  public void reserveStock(StockReservationRequestKnob reservationRequest) {
    stockService.reserveStock(reservationRequest.orderId(), reservationRequest.items());
  }

  public void confirmReservation(long orderId) {
    stockService.confirmReservation(orderId);
  }

  public void cancelReservation(Long orderId) {
    stockService.cancelReservation(orderId);
  }

  public int getStockByProductId(Long productId) {
    // I submit to them a PR that will be peer reviewed by 2+ devs owning inventory
//    int reservedQuantity = stockReservationRepo.sumReservedQuantityByProductId(productId);
    return stockRepo.findByProductId(productId).orElseThrow().items() /*- reservedQuantity*/;
    // Inventory teams corrects your code saying: we here decided to remove
    // from master stock AFTER the reservation is CONFIRMED
    // for that purpose, the reported stock level should be MASTER - SUM(all reservatiosn)
  }
}
