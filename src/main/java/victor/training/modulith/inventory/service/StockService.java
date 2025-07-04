package victor.training.modulith.inventory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.StockUpdatedEvent;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.model.StockReservation;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.inventory.repo.StockReservationRepo;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {
  private final StockRepo stockRepo;
  private final StockReservationRepo stockReservationRepo;
  private final ApplicationEventPublisher applicationEventPublisher;

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

  private void subtractStock(long productId, Integer count) {
    Stock stock = stockRepo.findByProductId(productId).orElseThrow();
    stock.remove(count);
    stockRepo.save(stock);
    applicationEventPublisher.publishEvent(new StockUpdatedEvent(productId, stock.items()));
  }

  @Transactional
  public void confirmReservation(long orderId) {
    log.info("Stock reservation confirmed for order {}", orderId);
    stockReservationRepo.deleteAllByOrderId(orderId);
  }

  public void cancelReservation(Long orderId) {
    // omitted for demo
  }

  public int getStockForProduct(long productId) {
    return stockRepo.findByProductId(productId).map(Stock::items).orElse(0);
  }
}
