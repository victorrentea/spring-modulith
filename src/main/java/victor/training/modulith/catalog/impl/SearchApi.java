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

    return productRepo.search(criteria.name, criteria.description, pageRequest)
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
