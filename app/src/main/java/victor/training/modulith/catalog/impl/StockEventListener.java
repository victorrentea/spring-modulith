package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.OutOfStockEvent;

@Service
@RequiredArgsConstructor
public class StockEventListener {
   private final ProductRepo productRepo;
   @EventListener
   @Transactional
    public void onOutOfStock(OutOfStockEvent event) {
       var product = productRepo.findById(event.productId()).orElseThrow();
       product.available(false);
    }
    @EventListener
    @Transactional
    public void onBackInStock(OutOfStockEvent event) {
        var product = productRepo.findById(event.productId()).orElseThrow();
        product.available(true);
    }
}
