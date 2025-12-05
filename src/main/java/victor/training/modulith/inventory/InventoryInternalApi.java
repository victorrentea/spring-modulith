package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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


  // code is owned by a different team
  // 1) Ask THEM to do it for you
  // 2) DIY and ask THEM for code review by 2+ devs
  public int getStock(Long id) {
    return stockRepo.findByProductId(id).orElseThrow().items(); // 💩
  }
}
