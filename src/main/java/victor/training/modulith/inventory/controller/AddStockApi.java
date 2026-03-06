package victor.training.modulith.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.StockUpdatedEvent;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;

@RestController
@RequiredArgsConstructor
public class AddStockApi {
  private static final Logger log = LoggerFactory.getLogger(AddStockApi.class);
  private final StockRepo stockRepo;
  private final ApplicationEventPublisher applicationEventPublisher;

  @PostMapping("stock/{productId}/add/{items}")
  @Transactional
  public void addStock(@PathVariable long productId, @PathVariable int items) {
    Stock stock = stockRepo.findByProductId(productId).orElse(new Stock().productId(productId));
    stock.add(items);
    stockRepo.save(stock);
    applicationEventPublisher.publishEvent(new StockUpdatedEvent(productId, stock.items()));
//    kafkaTemplate.send...
    log.info("might run in parallel with listner");
  }
}
