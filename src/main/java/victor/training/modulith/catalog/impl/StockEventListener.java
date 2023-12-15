package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.out.events.ProductBackInStockEvent;
import victor.training.modulith.inventory.out.events.ProductOutOfStockEvent;

@Component
@RequiredArgsConstructor
public class StockEventListener {
  private final ProductRepo productRepo;
  @EventListener
  @Transactional
  public void onOutOfStock(ProductOutOfStockEvent event) {
    productRepo.findById(event.productId()).orElseThrow().inStock(false);
  }
  @EventListener
  @Transactional
  public void onBackInStock(ProductBackInStockEvent event) {
    productRepo.findById(event.productId()).orElseThrow().inStock(true);
  }
}
