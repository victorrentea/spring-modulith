package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

  @EventListener
  @Transactional
  public void on(StockUpdatedEvent event) {
    productRepo.findById(event.productId())
        .orElseThrow().inStock(event.newStock() > 0);
  }
}
