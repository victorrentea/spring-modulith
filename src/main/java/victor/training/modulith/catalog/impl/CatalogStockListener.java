package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogStockListener {
  private final ProductRepo productRepo;


  // a) in-thread, sharing-tx
  // b) in-thread, different tx @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  // c) @Async: my listener might not find publisher tx commited yet = race
  // d) external broker @KafkaListener 🚀
  @EventListener
  @Transactional // ensure we can update Product when handling the event
  public void onStockUpdated(StockUpdatedEvent event) {
    var opt = productRepo.findById(event.productId());
    if (opt.isEmpty()) {
      log.warn("Received StockUpdatedEvent for unknown productId={} (stock={})", event.productId(), event.newStock());
      return;
    }

    Product product = opt.get();
    boolean newInStock = event.newStock() > 0;
    if (product.inStock() == newInStock) {
      log.debug("Product {} already has inStock={}, no change", product.id(), newInStock);
      return;
    }

    product.inStock(newInStock);
    productRepo.save(product);
    log.info("Updated Product {} inStock -> {} based on inventory stock {}", product.id(), newInStock, event.newStock());
  }
}
