package victor.training.modulith.inventory.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.OutOfStockEvent;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.OrderStatusChangedEvent;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReserveStockService {
  private final StockRepo stockRepo;
  private final StockReservationRepo stockReservationRepo;

  @Transactional
  public void reserveStock(long orderId, List<LineItem> items) {
    for (var item : items) {
      subtractStock(item.productId(), item.count());
      createReservation(orderId, item.productId(), item.count());
    }
  }

  private void createReservation(long orderId, long productId, Integer count) {
    StockReservation reservation = new StockReservation()
        .orderId(orderId)
        .productId(productId)
        .items(count);
    stockReservationRepo.save(reservation);
  }
private final ApplicationEventPublisher eventPublisher;
  private void subtractStock(long productId, Integer count) {
    Stock stock = stockRepo.findByProductId(productId).orElseThrow();
    stock.remove(count);
    if (stock.items() == 0) {
      eventPublisher.publishEvent(new OutOfStockEvent(productId));
    }
    stockRepo.save(stock);
  }

  @ApplicationModuleListener
  void onOrderPaid(OrderStatusChangedEvent event) {
    if (event.status() == OrderStatus.PAYMENT_APPROVED) {
      log.info("Stock reservation confirmed: " + event);
      stockReservationRepo.deleteAllByOrderId(event.orderId());
    }
  }

}
