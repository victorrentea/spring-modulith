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
  private final InventoryInternalApi inventoryInternalApi;

  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> call(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items in stock => SearchE2ETest
    // Fix for #1: prefetch all product Ids in stock
//    var tenMillionIds = inventoryInternalApi.getAllProductIdsInStock();
    // OOME risk + performance load!

    // Fix #1+#2: materialized view = CACHE eventually consistent to pre-compute
    // complex aggregations = too complex?


//    return productRepo.searchJoiningTheirView("%" + name + "%", pageRequest)

    // Fix #1+#2: "bring in" what you need to filter in your select
    // => data replication: store in OUR Product, a projection of THEIR data.
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)
        .stream()

//        .filter(p->inventoryRestApi.getStock(p.id))
        // 1) PERFORMANCE DISASTER REST call 50ms x 1 page(20) in a looP!! => latency +++
        // 2) BAD UX: pagination BEFORE filtering => the final page might have 19,15,3,0 items
                // not 20 as request!

        // Fix for #1+2 => have a 3rd "search component do all this"

        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
