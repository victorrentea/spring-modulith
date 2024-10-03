package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchProductApi {
  private final ProductRepo productRepo;

  public record ProductSearchResult(long id, String name) {
  }

  private final InventoryInternalApi inventoryInternalApi;
  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items in stock

    // OOME  if too many products in DB
//    Set<Long> all = inventoryInternalApi.findAllProductsInStock();

//    Set<Long> all = inventoryInternalApi.findAllProductsInStock(List<productIdsThatMatchedName>);

    // Best for modular monolith
// #1 write a JPQL joining a VIEW the inventory exposed in their public API

    // only option for microservices = DATA REPLICATION -> in my DB store part of their data
    // DOES not mean fetchAll in memory
    // #2 fire an event from the source of change.


//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)
        .stream()
        .filter(e -> inventoryInternalApi.getStock(e.id()) > 0)
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
