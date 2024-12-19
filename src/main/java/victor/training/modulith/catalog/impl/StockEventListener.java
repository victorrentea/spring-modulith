package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.StockUpdated;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventListener {

  private final ProductRepo productRepo;

  @EventListener // spring today, tomorrow Kafka/Rabbit lisneter
  public void onStockChanged(StockUpdated event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.items() > 0);
    productRepo.save(product);
  }
}
