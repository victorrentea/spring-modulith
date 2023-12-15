package victor.training.modulith.inventory.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import victor.training.modulith.catalog.CatalogModule;
import victor.training.modulith.inventory.app.entity.Stock;
import victor.training.modulith.inventory.app.repo.StockRepo;
import victor.training.modulith.inventory.out.events.ProductBackInStockEvent;

@Service
@RequiredArgsConstructor
public class StockService {
  private final StockRepo stockRepo;
//  private final CatalogModule catalogModule;// causes a cycle
  @Transactional
  public void addStock(@PathVariable long productId, @PathVariable int items) {
    Stock stock = stockRepo.findByProductId(productId).orElse(new Stock().productId(productId));
    stock.add(items);
    if (stock.items() == 0) {
//      catalogModule.f();
//      eventPublisher.publishEvent(new ProductBackInStockEvent(productId));
    }
    stockRepo.save(stock); // hibernate tells spring to fire an event for each entity that has registered events
    // during the .save() call all the event listeners for ProductBackInStockEvent are notified
    // in the same thread and sharing the existing tx :18
  }
  private final ApplicationEventPublisher eventPublisher;

  public Integer addStock(@PathVariable long productId) {
    return stockRepo.findByProductId(productId).map(Stock::items).orElse(0);
  }
}
