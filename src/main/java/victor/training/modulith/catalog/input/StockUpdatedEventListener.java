package victor.training.modulith.catalog.input;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Component
@RequiredArgsConstructor
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

  @ApplicationModuleListener
  public void on(StockUpdatedEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.newStock() > 0);
  }
}
