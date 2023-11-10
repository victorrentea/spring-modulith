package victor.training.modulith.inventory.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockData {
  private final StockRepo stockRepo;

  @EventListener(ApplicationStartedEvent.class)
  void initialData() {
    stockRepo.save(new Stock()
        .productId(1L)
        .add(100));
  }
}
