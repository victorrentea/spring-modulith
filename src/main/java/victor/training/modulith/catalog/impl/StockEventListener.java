package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.BackInStockEvent;
import victor.training.modulith.inventory.OutOfStockEvent;


@RequiredArgsConstructor
@Service
public class StockEventListener {
  private final ProductRepo productRepo;

//  @ApplicationModuleListener
//  void onOutOfStock(OutOfStockEvent event) {
//    Product product = productRepo.findById(event.productId()).orElseThrow();
//    product.inStock(false);
//    productRepo.save(product); // not really needed due to @Transactional on @ApplicationModuleListener
//  }
//
//  @ApplicationModuleListener
//  void onBackInStock(BackInStockEvent event) {
//    Product product = productRepo.findById(event.productId()).orElseThrow();
//    product.inStock(true);
//    productRepo.save(product);
//  }
}
