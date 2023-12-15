package victor.training.modulith.inventory.app;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import victor.training.modulith.inventory.app.entity.Stock;
import victor.training.modulith.inventory.app.repo.StockRepo;

@Component
@RequiredArgsConstructor
public class InitialData {
  private final StockRepo stockRepo;

  @EventListener(ApplicationStartedEvent.class)
  void initialData() {
    stockRepo.save(new Stock()
        .productId(1L)
        .add(100));
  }
}
