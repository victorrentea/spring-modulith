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


  // AVOID EVENTS UNLESS TRYING TO DECOUPLE A MICROSERVICE SOON

  @Transactional
  @EventListener
  // SURPRISE: event listeners in Spring run in the caller thread & transaction
  // hell  if @Async you use want

//  @Async // kept in mem until ran - prone to kill-9
//  @TransactionalEventListener(phase = AFTER_COMMIT) // .. of the publisher tx

//  @ApplicationModuleListener // inserts in a table the event until dispatched

  // @KafkaListener
  public void method(StockUpdatedEvent event) {
    var product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(event.newStock() != 0);
  }
}
