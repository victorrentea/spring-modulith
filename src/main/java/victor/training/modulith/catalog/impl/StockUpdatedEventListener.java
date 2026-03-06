package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

  // in what thread + transaction does this listener run vs event publisher?
  @EventListener // A) in same thread + tx === a method call
  // ✅/❌ all work is ATOMIC

//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)

//  @ApplicationModuleListener

  public void onStockUpdated(StockUpdatedEvent event) {
    log.info("Stock updated for productId={}, newStock={}", event.productId(), event.newStock());
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.newStock() > 0);
    productRepo.save(product);
  }
}
