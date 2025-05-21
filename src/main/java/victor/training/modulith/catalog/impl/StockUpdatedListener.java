package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.StockUpdatedEvent;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockUpdatedListener {
  private final ProductRepo productRepo;

  @EventListener
  @Transactional
  public void onStockUpdated(StockUpdatedEvent event) {
//    var productOpt = productRepo.findById(event.productId());
//
//    if (productOpt.isEmpty()) {
//      log.warn("Product not found for stock update: {}", event);
//    } else {
//      var product = productOpt.get();
//      product.inStock(event.newStock() > 0);
//      productRepo.save(product);
//      log.info("Updated stock for product {} to {}", product, event.newStock());
//    }
    // I could do this in a single functional chain, says Klaus
    productRepo.findById(event.productId())
        .ifPresent(product -> product.inStock(event.newStock() > 0));

  }
}
