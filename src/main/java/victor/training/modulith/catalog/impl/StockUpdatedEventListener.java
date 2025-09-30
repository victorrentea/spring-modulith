package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class StockUpdatedEventListener {
  private final ProductRepo productRepo;

//  @Async //NEVER: risky, volatile runs the listener on another thread => another transaction
//  @EventListener // by default runs in publisher thread & transaction
  // => delaying
  // => exception bubble back
  // => rollback in the publisher
  //- UNEXPECTED
  //- acritical flow (add stock in warehouse might ROLLBACK for a bug in a publisher)
  //+ perfect consistency
  // use events for
  // a) 2+ listener modules
  // b) staging 71if you plan to eject a module as a service and move to a broker Kafka,Rabb,ac..
//  @Transactional

  @ApplicationModuleListener // comits the event to consume in DB first. then async processes it
  public void on(StockUpdatedEvent event) { //fired by inventory team
    Product product = productRepo.findById(event.productId())
        .orElseThrow();// 💥 TODO
    product.inStock(event.newStock() > 0);
  } //UPDATE after you exit
}
