package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.StockChangedEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockChangedEventListener {
  private final ProductRepo productRepo;
  @EventListener
  public void onStockChanged(StockChangedEvent event) {
    log.info("Stock changed for product {} to {}", event.productId(), event.available());
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.available());
    productRepo.save(product);
  }
}
