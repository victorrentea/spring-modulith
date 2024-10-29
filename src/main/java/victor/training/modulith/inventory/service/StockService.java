package victor.training.modulith.inventory.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.StockUpdated;
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
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public void reserveStock(long orderId, List<LineItem> items) {
    for (var item : items) {
      subtractStock(item.productId(), item.count());
      createReservation(orderId, item.productId(), item.count());
      eventPublisher.publishEvent(new StockUpdated(item.productId(),item.count()));
      // the only transactional way to CHANGE MY DB AND PUBLISH AN EVENT is
      // eventRepo.save(new EventToSend(new StockUpdated(item.productId(),item.count())));
      // EVENT_TO_SEND

      // ⭐️ OUTBOX TABLE PATTERN ⭐️
      // a) @Scheduled/cron running every 5 sec and sending all the events from the outbox table to Kafka, deleting them afterwards
      // b) tell Kafka Connect to tail the commit log of this table and send to Kafka any row inside.
      // Bad news: the above can send duplicated events. = at-least-once delivery
      // so be sure to include in the event a way to deduplicate it. (like a UUID)
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
  }

  public void confirmReservation(long orderId) {
    log.info("Stock reservation confirmed for order {}", orderId);
    stockReservationRepo.deleteAllByOrderId(orderId);
  }

}
