package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Component;
import victor.training.modulith.inventory.BackInStockEvent;
import victor.training.modulith.inventory.OutOfStockEvent;


@Component
@RequiredArgsConstructor
public class StockEventListener {
  private final ProductRepo productRepo;
  @EventListener
  public void handleOutOfStockEvent(OutOfStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(false);
    productRepo.save(product);
  }

  @EventListener // the listeners RUN SYNCHRONOUSLY at publish() time
//  @ApplicationModuleListener // the listeners now runs async in a separate transaction, persisted in between in DB

  // the distributed reality is a bit more cruel: async;
  // ~= @KafkaListener/ @RabbitListener
  public void handleBackInStockEvent(BackInStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(true);
    productRepo.save(product);
  }
}


//