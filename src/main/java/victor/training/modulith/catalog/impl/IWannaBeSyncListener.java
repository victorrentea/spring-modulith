package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.StockUpdatedEvent;

@Slf4j
@RequiredArgsConstructor
@Service
public class IWannaBeSyncListener {
  private final ProductRepo productRepo;

  @EventListener
  public void on(StockUpdatedEvent event) { //fired by inventory team
   log.info("Here I am");
  } //UPDATE after you exit
}
