package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Service
@RequiredArgsConstructor
public class StockListener {
  //  @EventListener
//  public void onStockChange(StockChangeEvent event) {
//    log.info("Stock changed for product {} to {}", event.getProductId(), event.getNewStock());
//  }

  private final ProductRepo productRepo;
//  @EventListener
//  @Transactional
//  public void onOutOfStock(OutOfStockEvent event) {
//    Product product = productRepo.findById(event.getProductId()).orElseThrow();
//    product.inStock(false);
//  }
//  public void onBackInStock(BackInStockEvent event) {...
}
