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
    // TODO only return items which are currently in stock

    // ✅ 1 JOIN cross-module ⭐️modulith for long ±
//     return productRepo.search(criteria.name, criteria.description, pageRequest)
    // ✅ 2 REPLICATE DATA: copy stock info in my Product.stock = data replication 😱 ⭐️microservice soon
    // ❌ 3 in-memory join: you bring the smallest dataset first in your mem ->>> pass to 2nd source
     return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
//        .filter(product -> inventoryInternalApi.getStock(product.id()) > 0) // is stupid
        // 1) ❌Performance Massacre (N+1 method calls => query)
        // 2) ❌Pagination is screwed, as filtering post-pagination
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
