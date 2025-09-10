package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockProjectionListener {
  private final ProductRepo productRepo;

  @Transactional
  @EventListener
  public void onStockUpdated(StockUpdatedEvent event) {
    long productId = event.productId();
    int newStock = event.newStock();

    var optionalProduct = productRepo.findById(productId);
    if (optionalProduct.isEmpty()) {
      // product may not exist in catalog (yet); ignore gracefully
      log.debug("StockUpdatedEvent received for non-existing Product {}. Ignoring.", productId);
      return;
    }

    Product product = optionalProduct.get();
    boolean inStock = newStock > 0;
    if (product.inStock() != inStock) {
      product.inStock(inStock);
      productRepo.save(product);
      log.debug("Updated Product {} inStock={} due to StockUpdatedEvent({})", productId, inStock, newStock);
    }
  }
}
