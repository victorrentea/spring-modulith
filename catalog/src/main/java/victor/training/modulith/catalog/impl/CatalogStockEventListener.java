package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class CatalogStockEventListener {
  private final ProductRepo productRepo;

  @EventListener
  public void onStockUpdated(StockUpdatedEvent event) {
    productRepo.findById(event.productId()).ifPresentOrElse(product -> {
      boolean inStock = event.newStock() > 0;
      if (product.inStock() != inStock) {
        product.inStock(inStock);
        productRepo.save(product);
        log.info("Updated Product {} inStock -> {} (stock={})", product.id(), inStock, event.newStock());
      }
    }, () -> {
      log.warn("Received StockUpdatedEvent for unknown productId={}", event.productId());
    });
  }
}
