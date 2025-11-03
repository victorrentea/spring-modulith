package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.api.inventory.BackInStockEvent;
import victor.training.modulith.shared.api.inventory.OutOfStockEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventListener {
  private final ProductRepo productRepo;

  @EventListener
  public void onOutOfStockEvent(OutOfStockEvent event) {
    var product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(false);
    productRepo.save(product);
  }
  @EventListener
  //@KafkaListener
  public void onBackInStockEvent(BackInStockEvent event) {
    var product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(true);
    productRepo.save(product);
  }

}
