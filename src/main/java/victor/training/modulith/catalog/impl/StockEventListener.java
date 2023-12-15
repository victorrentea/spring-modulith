package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.out.events.ProductBackInStockEvent;
import victor.training.modulith.inventory.out.events.ProductOutOfStockEvent;

// goes in "in.event" in a hexagon
@Component
@RequiredArgsConstructor
public class StockEventListener {
  private final ProductRepo productRepo;
  @ApplicationModuleListener
  // async, of an event persisted in between in a table
  public void onOutOfStock(ProductOutOfStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
//    rest api call   // DANGER. tx starvation
    product.inStock(false);// this is COMMITED with set stock = 0 in stock module.

  }
  @EventListener
  @Transactional
  public void onBackInStock(ProductBackInStockEvent event) {
    productRepo.findById(event.productId()).orElseThrow().inStock(true);
  }
}
