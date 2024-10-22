package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageListener {
  private final ProductRepo productRepo;

//  @KafkaListener
  @EventListener
  public void onStockUpdate(StockUpdateEvent event) {
    log.info("Received stock update event: {}", event);

    productRepo.findById(event.getProductId())
        .ifPresent(product -> {
          product.setStock(event.getNewStock());
          productRepo.save(product);
        });
  }
}
