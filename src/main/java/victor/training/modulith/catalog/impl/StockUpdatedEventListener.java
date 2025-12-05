package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

//  @EventListener
//  @Transactional

  @ApplicationModuleListener
  // @KafkaListener on their CDC
  public void on(StockUpdatedEvent event) {
    log.info("Start");
    productRepo.findById(event.productId())
        .orElseThrow().inStock(event.newStock() > 0);
    log.info("End⭐️");
  }
  // at the end of this @Transactional JPA will see the entity loaded at :19 was changed :20
  // and auto-UPDATE that Product
}
