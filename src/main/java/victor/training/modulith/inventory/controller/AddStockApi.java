package victor.training.modulith.inventory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.catalog.CatalogInternalApi;
import victor.training.modulith.inventory.StockUpdated;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;

@RestController
@RequiredArgsConstructor
public class AddStockApi {
  private final StockRepo stockRepo;
  private final ApplicationEventPublisher applicationEventPublisher;
//  private final CatalogInternalApi catalogInternalApi;

  @PostMapping("stock/{productId}/add/{items}")
  @Transactional
  public void execute(@PathVariable long productId, @PathVariable int items) {
    Stock stock = stockRepo.findByProductId(productId).orElse(new Stock().productId(productId));
//    boolean backInStock = stock.items() == 0;
    var oldStock = stock.items();
    stock.add(items);
    stockRepo.save(stock);

    // REST API call tomorrow
//    catalogInternalApi.setStock(productId, stock.items());
    // I need to tell catalog-service that I have stock

    // imagine kafkaTemplate.send...
//    var event = new NewStockCreated;
    // not a fact, a dummy meaningless word. as well as: Util, Helper, Data, Info
//    var event = new ManageStock();

    // event = fact that has happened in the past that others might be interested in
    var event = new StockUpdated(productId, stock.items()); // Integration Event
    // ± less type of events, used also to tell when out of stock
    // * more events sent
    // - no business intent ~ PUT/PATCH> the listener does not know WHY this event fired
    // + it brings all the data

//    if (oldStock == 0) {
//      var event = new BackInStock(productId); // Domain Event
//      // + carries intent, not just data
    // - more types of events 🤯
//      var event = new BackInStock(productId, stock.items()); // "EventShot" = Event + Data
//      // + also carry data
//    }
    applicationEventPublisher.publishEvent(event);
  }
}





