package victor.training.modulith.inventory.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.order.OrderConfirmedEvent;
import victor.training.modulith.shared.ProductId;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReserveStockService {
  private StockRepo stockRepo;
  private StockReservationRepo stockReservationRepo;

  @Transactional
  public void reserveStock(long orderId, Map<ProductId, Integer> items) {
    for (var entry : items.entrySet()) {
      ProductId productId = entry.getKey();
      Integer count = entry.getValue();
      subtractStock(productId, count);
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

  private void subtractStock(ProductId productId, Integer count) {
    Stock stock = stockRepo.findByProductId(productId).orElseThrow();
    stock.remove(count);
    stockRepo.save(stock);
  }

  @ApplicationModuleListener
  void onOrderConfirmed(OrderConfirmedEvent event) {
    stockReservationRepo.deleteAllByOrderId(event.orderId());
  }

}
