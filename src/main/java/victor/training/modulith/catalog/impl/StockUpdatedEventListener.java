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

  @EventListener // by default runs in publisher thread & transaction
  // => delaying
  // => exception bubble back
  // => rollbackin the publisher
  //- UNEXPECTED
  //- acritical flow (add stock in warehouse might ROLLBACK for a bug in a publisher)
  //+ perfect consistency
  @Transactional
  public void on(StockUpdatedEvent event) { //fired by inventory team
    Product product = productRepo.findById(event.productId())
        .orElseThrow();// 💥 TODO
    product.inStock(event.newStock() > 0);
  } //UPDATE after you exit
}
