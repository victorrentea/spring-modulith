package victor.training.modulith.catalog.impl;

import jdk.jfr.TransitionTo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.BackInStockEvent;
import victor.training.modulith.inventory.OutOfStockEvent;


@RequiredArgsConstructor
@Service
public class StockEventListener {
  private final ProductRepo productRepo;

  @EventListener
  @Transactional
  public void onOutOfStock(OutOfStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(false);
  }
  @EventListener
  @Transactional
  public void onBackInStock(BackInStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(true);
  }
}
