package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.BackInStockEvent;
import victor.training.modulith.inventory.OutOfStockEvent;


@RequiredArgsConstructor
@Service
public class StockEventListener {
  private final ProductRepo productRepo;

//  @EventListener // by default
  // ruleaza in acelasi thread si aceeasi tranzactie ca inventory (care a aruncat eventul)
  // blocand executia lui si potential cauzandu-i ROLLBACKuri daca tu crapi
  @Transactional
  @ApplicationModuleListener // ruleaza in thread separat si tranzactie separata
  // motz peste: Spring MOdulith poate optional sa PERSISTE IN DB eventurile pana sunt procesate
  public void onOutOfStock(OutOfStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(false);
  }
  @ApplicationModuleListener
  @Transactional
  public void onBackInStock(BackInStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(true);
  }
}
