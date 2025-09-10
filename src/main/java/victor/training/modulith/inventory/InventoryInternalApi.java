package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
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
  // a) pui o sedinta (max o luna de acum) in care sa-i rogi sa-ti faca (poate) feat anu asta
  // b) imi fac damblaua dupa cum cred eu, si apoi le ridic PR/MRrr sa-mi faca review cu min 4 ochi (adica 2 de-ai lor jr/sr)
  // c) le incalc incapsularea (adica-mi injectez Repoul lor) si pun un // FIXME blue-team cand aveti timp Q urmator

  // echipa owner scrie/modifica testele

  public int getStock(long productId) {
    Integer master = stockRepo.findByProductId(productId).map(Stock::items).orElse(0);
//    int reserved = stockReservationRepo.getStockReservationsByProductId(productId)
//        .stream().mapToInt(r -> r.items()).sum();
    return master/*reserved*/;
  }
}
