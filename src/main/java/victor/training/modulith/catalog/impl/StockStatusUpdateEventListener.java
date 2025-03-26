package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.StockStatusUpdatedEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockStatusUpdateEventListener {
  private final ProductRepo productRepo;

  @EventListener
  public void on(StockStatusUpdatedEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.inStock());
    productRepo.save(product);
  }
}
