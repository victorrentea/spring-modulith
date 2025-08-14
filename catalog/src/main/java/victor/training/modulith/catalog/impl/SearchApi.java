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
public class SearchApi {
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventoryInternalApi;

  public record ProductSearchCriteria(String name, String description) { }

  public record ProductSearchResult(long id, String name) {  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(
      @RequestParam ProductSearchCriteria criteria,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO CR: only return items which are currently in stock
//    return productRepo.searchView(criteria.name, criteria.description, pageRequest)
    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
//        .filter(p-> stockRepo.findByProductId(p.id()).get().items() > 0)
//        .filter(p->inventoryInternalApi.getStockByProductId(p.id()) > 0)
        // #respected their encapsulation, but still left with 2 mistakes:
        // #1 for {SELECT - N+1 query
        // #2 SQL returned 20 (one page), filter left 5, UX = 🚽
        // STUPID let's select 40 just in case filter leaves less than 20 let's select for more 40 until we get
        // you can't filter AFTER pagination.
        // Fix1(Modulith FTW): join THEIR view (this is the ONLY exception allowing you to SELECT from THEIR schema) = in ADR
        // Fix2(plan to microservice) = replicate THEIR data.
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
