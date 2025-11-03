package victor.training.modulith.inventory.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.shared.api.inventory.BackInStockEvent;

@RestController
@RequiredArgsConstructor
public class AddStockApi {
  private final StockRepo stockRepo;
  private final ApplicationEventPublisher eventPublisher;

  @PostMapping("stock/{productId}/add/{items}")
  @Transactional
  public void addStock(@PathVariable long productId, @PathVariable int items) {
    Stock stock = stockRepo.findByProductId(productId).orElse(new Stock().productId(productId));
    if (stock.items() == 0) {
      eventPublisher.publishEvent(new BackInStockEvent(productId));
    }
    stock.add(items);
    stockRepo.save(stock);
  }
}
