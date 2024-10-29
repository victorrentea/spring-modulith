package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.StockUpdated;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventListener {
  private final ProductRepo productRepo;
//  @ApplicationModuleListener // saves in DB events until dispatched
  @org.springframework.context.event.EventListener
  @Transactional
  public void method(StockUpdated event) {
    System.out.println("Stock updated: " + event);

    var product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.stock() != 0);
  }
}
