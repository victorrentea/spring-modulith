package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.ItemBackInStockEvent;
import victor.training.modulith.inventory.ItemRanOutOfStockEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventListener {
  private final ProductRepo productRepo;

  @EventListener
  @Transactional
  public void on(ItemRanOutOfStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(false);
  }

  @EventListener
  @Transactional
  public void on(ItemBackInStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(true);
  }
}
