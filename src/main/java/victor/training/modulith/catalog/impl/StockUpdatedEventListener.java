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

  // 1) in-thread, in-transaction of the publisher ~ hidden method call
//  @EventListener
//  @Transactional
  // + consistency
  // - fragile/coupled

  // 2) persisted async events between modules: publisher module save its (work + event) commit;
  @ApplicationModuleListener // spring-modulith

  // 3) @Kafka/RabbitListener
  public void on(StockUpdatedEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.newStock() > 0);
  }
}
