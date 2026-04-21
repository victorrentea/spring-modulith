package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.inventory.repo.StockRepo;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchApi {
  private final ProductRepo productRepo;
  private final StockRepo stockRepo;
  private final InventoryInternalApi inventoryInternalApi;

  public record ProductSearchCriteria(String name, String description) { }

  public record ProductSearchResult(long id, String name) {  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(
      @RequestParam ProductSearchCriteria criteria,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items which are currently in stock

//    var oom = productRepo.findAll();// 💥

    // Cross-module query
    // a) join their view 🤨: (decent if on a longlived modular monolith)
    //  🙁 The database incremental scripts of the 2 modules now have a
    //  temporal coupling about what gets created before what
    //  🙁🙁🙁🙁 If catalog wants to write a module test for itself because it joins the view,
    //  it will have to fill up the tables of inventory underlying the view.
    //  From another perspective, you could say that you cannot mock a view

    // b) data replication: adding to Product.inStock kept in sync how?
    //    - intra-db replication/PLSQL/TRIGGERS (hard, vendor lockin) ❌❌ if tomorrow microservices🦄
    //    - ❌PUSH-CHANGE inventory would [REST] call catalog whenever they update their stock?
    //      ARCH RULE: You do not couple the data owner(invetory)
    //        > to the MANY/OFFLINE/BUG/TIMEOUT listeners(catalog).
    //    ⇒ Events (today:in-mem, tomorrow:Kafka,Rabbit,ServiceBus..)⭐️⭐️⭐️
    //      make today listeners run async (⇒ separate tx) but durable

    return productRepo.search(criteria.name, criteria.description, pageRequest/*, inMax1000*/)
        .stream()
//        .filter(product -> stockRepo.findByProductId(product.id()).orElseThrow().items() > 0) ❌

//        .filter(product -> inventoryInternalApi.getStockByProductId(product.id()) > 0) ❌
        // ☠️A) N+1 QUERY PROBLEM : hitting DB in a LOOP: 20 + 1 => prefetch all stocks in memory
        //    -> idsInStock.contains(product.id())
        // ☠️B) filter after LIMIT => you show 15 elements not 20 on page #1
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
