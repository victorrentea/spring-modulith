package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.BackInStockEvent;
import victor.training.modulith.inventory.OutOfStockEvent;

@Component
@RequiredArgsConstructor
public class StockEventListener {
  private final ProductRepo productRepo;
  // runs in the same tx as the publisher => the CATALOG.PRODUCT.IN_STOCK column is updated in the same tx
  // as INVENTORY.STOCK.ITEMS
  @EventListener
  @Transactional
  public void onStockChanged(OutOfStockEvent event) {
    productRepo.findById(event.productId()).ifPresent(product -> {
      product.inStock(false);
    });
  }
  @EventListener
  @Transactional
  public void onStockChanged(BackInStockEvent event) {
    productRepo.findById(event.productId()).ifPresent(product -> {
      product.inStock(true);
    });
  }
}