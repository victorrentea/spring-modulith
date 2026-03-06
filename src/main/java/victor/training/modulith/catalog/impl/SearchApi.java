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
 // VSA Jimmy Bogard
  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(
      @RequestParam ProductSearchCriteria criteria, // by ?name= and &desc=
      @RequestParam(required = false) PageRequest pageRequest // sql-side pagination: # page you want, # items/page, sorting
  ) {
    // TODO CR: only return items which are currently in stock

    // Rule: WHERE > ORDER > PAGINATE (limit/offset)
    // Option A (Modulith-way) = JOIN to a VIEW of another module ✅

    // Option B (Microservice-way) = replicate their data involved in your filtering✅
    // ☢️ kept in sync
    // - nightly jobs / ETL 1/day-hour
    // - trigger IFF still in the same DB instance + 2000s nostalgia
    // - sync push: ❌inventory REST calls catalog+3 others on every stock update (pressure + fragile)
    // - sync pull via polling: ❌catalog GET from inventory new updates since 1-10-30s ago
    // - ⭐️by events StockUpdatedEvent owned and triggered by inventory module ✅
    // - debezium.io -> Change Data Capture: DB plugin auto-firing a Kafka event on row update/insert/delete

    // Option C (Desperados): in-memory join❌
    // var products = productRepo.search...
    // List<Long> productIdsMatchingCriteria = products.extact Id
    // Map|Dictionary<Long productId, Integer stock> stocks = inventoryInternalApi.getStockForProducts(productIdsMatchingCriteria);
    //     => SELECT ... WHERE PRODUCT_ID IN (?, ?..?)
    // return products.filter(only those with stock > 0 in stocks) => may return 7 items, not 10 (page size)
    // if after filtering, less than a page remains => go fetch another page and repeat the process until you complete the page.

    // Option C2: ❌
    // var list10kLongs = inventoryInternalApi.findAllProductIdsInStock(); ±💥 OOME
    // var results = productRepo.search(criteria, list10k❌(max 1000 x ?), pageRequest)

    // Option D - push the search to a separate service Elastic Search⭐️ kept up to date by updates from BOTH services (catalog, stock)
    // a) push
    // b) notify

    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
        // Where( ~ LINQ C#
        .filter(product -> inventoryInternalApi.getStockByProduct(product.id()).orElse(1) > 0)
        // ❌ 3 items in stock (not 10 as page size) => UX 🤮 !! WHERE/filtering AFTER db pagination
        // ❌ N+1 query problem = SELECT in a loop
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
