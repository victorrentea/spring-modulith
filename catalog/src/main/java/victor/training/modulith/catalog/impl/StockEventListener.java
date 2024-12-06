package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.InventoryItemUpdatedEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventListener {

  private final ProductRepo productRepo;

  @EventListener
  public void onStockUpdate(InventoryItemUpdatedEvent event) {
    log.info("Stock updated for product {} in warehouse {}: {}",
        event.productId(), event.warehouseId(), event.stock());
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.stock() > 0);
    productRepo.save(product);
  }
}
