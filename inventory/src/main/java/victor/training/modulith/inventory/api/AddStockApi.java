package victor.training.modulith.inventory.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.shared.api.inventory.StockUpdatedEvent;

@RestController
@RequiredArgsConstructor
public class AddStockApi {
  private final StockRepo stockRepo;
  private final ApplicationEventPublisher applicationEventPublisher;

  @PostMapping("stock/{productId}/add/{items}")
  @Transactional
  public void call(@PathVariable long productId, @PathVariable int items) {
    Stock stock = stockRepo.findByProductId(productId).orElse(new Stock() .productId(productId));
    stock.add(items);
    stockRepo.save(stock);
    applicationEventPublisher.publishEvent(new StockUpdatedEvent(productId, stock.items()));
  }
}
