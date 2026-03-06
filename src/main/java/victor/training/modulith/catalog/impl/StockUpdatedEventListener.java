package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

  @ApplicationModuleListener
  public void onStockUpdated(StockUpdatedEvent event) {
    log.info("Stock updated for productId={}, newStock={}", event.productId(), event.newStock());
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.newStock() > 0);
  }
}
