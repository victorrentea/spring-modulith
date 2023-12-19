package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.ItemBackInStockEvent;
import victor.training.modulith.inventory.ItemRanOutOfStockEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockEventListener {
  private final ProductRepo productRepo;

  @EventListener
//  @Async // separate thread, separate tx. God help us all.
//  @Transactional
  @ApplicationModuleListener // spring
  // SpringModulith
  // saves in a DB table the event  when publshed in the tx of the publisher.
  // then with a scheduler, tries to dispatch these events, deleting them afterwards

  // USing a separate queue
  // we are doing that for 20years.
  // it's "Service Activator" JavaEE pattern : send ourselves a message over rabbit/JMS durable queue
  public synchronized void on(ItemRanOutOfStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(false);
  }

  @EventListener
  @Async // separate thread, separate tx. God help us all.
  @Transactional
  public void on(ItemBackInStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(true);
  }
}
