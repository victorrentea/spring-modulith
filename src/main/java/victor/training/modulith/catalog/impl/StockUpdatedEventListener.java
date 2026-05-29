package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void on(StockUpdatedEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.newStock() != 0);
//    if (true) throw new RuntimeException("BUG🐞");
    productRepo.save(product);
  }
}
