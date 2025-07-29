package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.api.inventory.StockUpdatedEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

  //  @KafkaListener
  @EventListener
  public void onStockUpdated(StockUpdatedEvent event) {
    long productId = event.productId();
    int newStock = event.newStock();
    Product product = productRepo.findById(productId).orElseThrow();
    product.inStock(newStock > 0);
    productRepo.save(product);
  }
}
