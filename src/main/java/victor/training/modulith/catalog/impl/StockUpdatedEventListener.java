package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.modulith.inventory.StockUpdatedEvent;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

//  @EventListener //  by default runs in publisher thread ± @Transaction 😱
  // 𐀏 consistency
  // ⊖ error in listener rollbacks publishers' transaction😱 != message bbroker

//  @Async // moves the listener on another thread + transaction❌
  // ☢️Risk: listener might RUN in thread#2 BEFORE publisher thread#1 does COMMIT❌
  // ☢️Risk: after publish, commit of publisher FAILS -> ROLLBACK

  // only fire the event after the COMMIT of the publisher tx
//  @TransactionalEventListener(phase = AFTER_COMMIT)
//  @Async
  // ☢️Risk: NOT DURABLE: if app crashes/shutdowm right before this metnhod runs ->


  @ApplicationModuleListener // async + after commit + persisted in table until processed

//  @KafkaListener// tomorrow
  public void onEvent(StockUpdatedEvent event) {
    log.info("IN listener");
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.newStock() > 0);
    productRepo.save(product);
  }
}
