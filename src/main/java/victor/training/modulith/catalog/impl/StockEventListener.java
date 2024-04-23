package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.BackInStockEvent;
import victor.training.modulith.inventory.OutOfStockEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventListener
{
    private final ProductRepo productRepo;

    @EventListener
    public void onOutOfStock(OutOfStockEvent event) {
        log.info("Received OutOfStockEvent for productId={}", event.productId());
        Product product = productRepo.findById(event.productId()).orElseThrow();
        product.inStock(false);
        productRepo.save(product);
    }

    @EventListener
    public void onBackInStock(BackInStockEvent event) {
        log.info("Received BackInStockEvent for productId={}", event.productId());
        Product product = productRepo.findById(event.productId()).orElseThrow();
        product.inStock(true);
        productRepo.save(product);
    }
}
