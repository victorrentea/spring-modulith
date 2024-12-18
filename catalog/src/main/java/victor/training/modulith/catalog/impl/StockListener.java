package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.shared.internalapi.inventory.events.BackInStockEvent;
import victor.training.modulith.shared.internalapi.inventory.events.OutOfStockEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockListener {
  private final ProductRepo productRepo;

  @EventListener
  @Transactional
  public void onOutOfStock(OutOfStockEvent event) {
    log.info("StockListener: Out of stock for product " + event.productId());
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(false);
  }

  @EventListener
  @Transactional
  public void onBackInStock(BackInStockEvent event) {
    log.info("StockListener: Back in stock for product " + event.productId());
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(true);
  }
}
