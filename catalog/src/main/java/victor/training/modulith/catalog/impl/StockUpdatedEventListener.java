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

  //  private final Map<Long, Integer> stock
  @EventListener // transition phase to
//  @KafkaLister @RabbitLister ...
  public void method(StockUpdatedEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.availableStock(event.newStock() != 0);
    productRepo.save(product);
  }
}
