package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.shared.ProductId;
import victor.training.modulith.inventory.impl.Stock;
import victor.training.modulith.inventory.impl.StockRepo;
import victor.training.modulith.inventory.impl.StockReservation;
import victor.training.modulith.inventory.impl.StockReservationRepo;
import victor.training.modulith.order.OrderConfirmedEvent;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryDoor {
  private final StockRepo stockRepo;
  private final StockReservationRepo stockReservationRepo;

  public int getStock(ProductId productId) {
    return stockRepo.findByProductId(productId).map(Stock::items).orElse(0);
  }


  @Transactional
  public void reserveStock(long orderId, Map<ProductId, Integer> items) {
    for (var entry : items.entrySet()) {
      ProductId productId = entry.getKey();
      Integer count = entry.getValue();
      substractStock(productId, count);
      createReservation(orderId, productId, count);
    }
  }

  private void createReservation(long orderId, ProductId productId, Integer count) {
    StockReservation reservation = new StockReservation()
        .orderId(orderId)
        .productId(productId)
        .items(count);
    stockReservationRepo.save(reservation);
  }

  private void substractStock(ProductId productId, Integer count) {
    Stock stock = stockRepo.findByProductId(productId).orElseThrow();
    stock.remove(count);
    stockRepo.save(stock);
  }

  @ApplicationModuleListener
  void onOrderConfirmed(OrderConfirmedEvent event) {
    stockReservationRepo.deleteAllByOrderId(event.orderId());
  }
}
