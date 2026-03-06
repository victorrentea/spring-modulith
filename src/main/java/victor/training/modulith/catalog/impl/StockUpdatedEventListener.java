package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

  // in what thread + transaction does this listener run vs event publisher?
//  @EventListener // A) in same thread + tx === a method call
  // ✅/❌ all work is ATOMIC
  // 🤯 if abused: harder to trace a method call througout the codebase

  // ------ inconsistencies might happen bellow 😈

  // B) same thread, but after commit of publisher tx => different tx
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  // ± @Transactional
  // 😱 publisher might commit ✅, listener might rollback ❌ > can you detect it?

  // C) different thread => different tx
//  @EventListener
//  @Async // RACE BUGS😱😱⚠️
  // ± @Transactional
  // 😱 Not durable: events are fired via memory
  //    ⇒ events get lost on server crash/restart💥 > can you detect this?


  // ⭐️⭐️ durable messages ----

  // D) via outbox table: publish=insert event into a table; consume=delete the event from table;
  @ApplicationModuleListener //(spring-modulith)

  // E) via external message broker === as if microservices
  // @Rabbit/KafkaListener

  public void onStockUpdated(StockUpdatedEvent event) {
    log.info("Stock updated for productId={}, newStock={}", event.productId(), event.newStock());
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.newStock() > 0);
    productRepo.save(product);
  }
}
