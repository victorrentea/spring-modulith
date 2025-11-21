package victor.training.modulith.catalog.impl;

import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.StockUpdatedEvent;

import java.beans.Transient;

@Component
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

  public StockUpdatedEventListener(ProductRepo productRepo) {
    this.productRepo = productRepo;
  }

  @EventListener // @KafkaLIstener/Rabbit/SQS...: runs in publisher thread & transasction = perfectly in sync
  // @Async @TransactionalEventListener(AFTER_COMMIT): runs after their commit; => possible inconsisntecies
//  @ApplicationModuleListener // async + persisted
  @Transactional
  public void onStockUpdated(StockUpdatedEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.newStock() > 0);
  }
}
