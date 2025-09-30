package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.model.Stock;
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
    Stock stock = stockRepo.findByProductId(productId).orElseThrow();
    int reserved = stockReservationRepo.getStockReservationsByProductId(productId)
        .stream()
        .mapToInt(StockReservation::items)
        .sum();
    return stock.items() /*- reserved*/; // an elder from inventory might correct your code
  }
  // Option1: raise inventory team a PR ->wait MORE
  // Option1b: you invite an inventory to pair program with you
  // Option2☢️ (open-source model): DIY in their code, they should review it x2 ->wait
  // Option3☢️: DIY in YOUR code and put //FIXME @blue-team correct and internalize this

  // You, from catalog don't have the necessary domain expertise to change correctly
  // feature-teams / component-teams
}
