package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryModuleApi;
import victor.training.modulith.inventory.model.Stock;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class SearchProductApi {
  private final ProductRepo productRepo;
  private final InventoryModuleApi inventoryModuleApi;

  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam PageRequest pageRequest) {
    // TODO CR: only return items in stock

//    List<Long> allInStock = inventoryModuleApi.findAllProductIdsInStock();
//    return productRepo.searchByNameLikeIgnoreCase(name, pageRequest)
//        .stream()
//        .filter(p -> allInStock.contains(p.id())) //removes items from 20 items -> 17 only
//        .map(e -> new ProductSearchResult(e.id(), e.name()))
//        .toList();

//    List<Long> allInStock = inventoryModuleApi.findAllProductIdsInStock();
//    return productRepo.searchByNameLikeIgnoreCase(name, pageRequest, allInStock)
//        .stream()
//        .map(e -> new ProductSearchResult(e.id(), e.name()))
//        .toList();

//    List<Long> allInStock = inventoryModuleApi.findAllProductIdsInStock();
//    return productRepo.searchByNameLikeIgnoreCase(name, pageRequest) // name = "e"
    // Trick: min 3 chars in search form to restrict the list to a decent size @Martin
//        .stream()
//        .map(e -> new ProductSearchResult(e.id(), e.name()))
//        .toList();

    // What about: CATALOG.PRODUCT JOIN INVENTORY.STOCK WHERE STOCK.ITEMS > 0 AND CATALOG.PRODUCT.NAME LIKE :name OFFSET :offset LIMIT :limit
    // Problem: locks inventory tables up.
    // OKish if you join only a VIEW exposed explicitly by the inventory: JOIN INVENTORY.STOCK_VIEW

    // option 4: replication via events.
    //   a) OutOfStockEvent, BackInStockEvent
    //   b) StockChangedEvent(productId) + listener calls back to inventory.getStock(productId)
    //   @EventListener events are processed in the same thread/transaction as the one that triggered
    // Risk: fri-evenning fix UPDATE SET but you forget to update the CATALOG.PRODUCT table


    // option5: push all data in an ElasticSearch

    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue(name, pageRequest)
        .stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList()
        ;


    // - move the search into the inventory? how to search by catalog.product.name
    // - create a higher level module that calls both inventory and catalog
  }

//  public void f(Stock stock) {
//    Product product = productRepo.findById(stock.productId()).orElseThrow();
//    if (prod)
//  }

  public record ProductSearchResult(long id, String name) {
  }
}
