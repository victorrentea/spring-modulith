package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

//  @EventListener // 1) runs in-thread & in-tx with publisher
  @ApplicationModuleListener // 2) dispatches the event async, persisting it in DB in the meantime
  // tomorrow: 3) @RabbitListener/ @KafkaListener
  public void onStockUpdated(StockUpdatedEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.available(event.newStock() > 0);
    productRepo.save(product);
    // exception bubbles up in publisher
    // I run in publisher transaction (if any)
  }
}
