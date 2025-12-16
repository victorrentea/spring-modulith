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

@Slf4j
@RequiredArgsConstructor
@Service
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

  // flavors of eventing in modulith
// #1 sync in publisher thread ± transaction ❤️±
// listner failure rollback publisher😱
//  @EventListener

  // #2 ❌ async after commit of publisher :
  // 😱 fragile
//  @Async
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)

  //#3 asyc + persisted
//  @Sqs/Rabbit/KafkaListener - as close to microservice⭐️⭐️⭐️
  @ApplicationModuleListener // - save in DB message until dispatched spring-modulith
  public void on(StockUpdatedEvent event) {
    System.out.println("ListenerY");
    System.out.println("ListenerY");
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.newStock() > 0);
//    if (true) throw new RuntimeException("BUG🐞"); // stops products from selling 😱😱😱😱
    System.out.println("ListenerX");
  }
}
