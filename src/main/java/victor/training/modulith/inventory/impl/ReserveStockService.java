package victor.training.modulith.inventory.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.order.OrderStatusChangedEvent;
import victor.training.modulith.shared.LineItem;
import victor.training.modulith.shared.ProductId;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReserveStockService {
  private StockRepo stockRepo;
  private StockReservationRepo stockReservationRepo;

  @Transactional
  public void reserveStock(long orderId, List<LineItem> items) {
    for (var item : items) {
      subtractStock(item.productId(), item.count());
      createReservation(orderId, item.productId(), item.count());
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
  void onOrderConfirmed(OrderStatusChangedEvent event) {
    stockReservationRepo.deleteAllByOrderId(event.orderId());
  }

}
