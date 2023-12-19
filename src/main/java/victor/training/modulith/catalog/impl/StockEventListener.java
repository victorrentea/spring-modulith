package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
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
  @Async // separate thread, separate tx. God help us all.
  @Transactional
  public void on(ItemRanOutOfStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(false);
  }

  @EventListener
  @Async // separate thread, separate tx. God help us all.
  @Transactional
  public void on(ItemBackInStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(true);
  }
}
