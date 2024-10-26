package victor.training.modulith.inventory.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import victor.training.modulith.inventory.StockChangedEvent;
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

  @GetMapping("force-reset-status-in-ca-trebuie#life")
  public void publishEventsForAllStockIfAbailabeOrNot() {
    for (Stock stock : stockRepo.findAll()) {
      if (stock.items() > 0) {
        eventPublisher.publishEvent(new StockChangedEvent(stock.items(), false));
      } else {
        eventPublisher.publishEvent(new StockChangedEvent(stock.items(), true));
      }
    }

  }

  @Transactional
  public void reserveStock(long orderId, List<LineItem> items) {
    for (var item : items) {
      subtractStock(item.productId(), item.count());
      createReservation(orderId, item.productId(), item.count());
      if (item.count()==0 ) {
        eventPublisher.publishEvent(new StockChangedEvent(item.productId(), false));
      }
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
