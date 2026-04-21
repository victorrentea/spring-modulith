package victor.training.modulith.inventory;

import jakarta.validation.constraints.NotNull;
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

  public int getStockByProductId(long productId) {
    // 1) DIY in the other module + submit PR to the CODEOWNER Component-Team/SME
    // 2) raise a ticket to them 🙏🙏🙏🙏🙏🙏
    var reserved = stockReservationRepo.getStockReservationsByProductId(productId)
      .stream()
      .mapToInt(StockReservation::items)
      .sum();

    return stockRepo.findByProductId(productId).orElseThrow().items() /*- reserved*/;
  }
}
