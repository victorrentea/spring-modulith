package victor.training.modulith.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.shared.api.inventory.events.BackInStockEvent;

@RestController
@RequiredArgsConstructor
public class AddStockApi {
  private final StockRepo stockRepo;
  private final ApplicationEventPublisher eventPublisher;
  @PostMapping("stock/{productId}/add/{items}")
  @Transactional
  public void execute(@PathVariable long productId, @PathVariable int items) {
    if( items < 0) {
      throw new IllegalArgumentException("items must be positive");
    }
    Stock stock = stockRepo.findByProductId(productId).orElse(new Stock().productId(productId));
    if (stock.items() == 0) { // can I remove this ?
      eventPublisher.publishEvent(new BackInStockEvent(productId));
    }
    stock.add(items);
    stockRepo.save(stock);
  }
}
