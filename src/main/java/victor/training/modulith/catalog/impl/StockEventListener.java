package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.OutOfStockEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventListener {
  private final ProductRepo productRepo;

  @EventListener
  public void onStockEvent(OutOfStockEvent event) {
    var product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(false);
    productRepo.save(product);
  }
  @EventListener
  public void onOutOfStock(OutOfStockEvent event) {
    var product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(false);
    productRepo.save(product);
  }

}
