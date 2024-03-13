package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import victor.training.modulith.inventory.impl.StockAvailabilityChangedEvent;

@Component
@RequiredArgsConstructor
public class StockEventListener {
  private final ProductRepo productRepo;
  @EventListener
  public void onOutOfStock(StockAvailabilityChangedEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.inStock());
    productRepo.save(product);
  }
}
