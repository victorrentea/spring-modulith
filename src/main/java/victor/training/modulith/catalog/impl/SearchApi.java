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
      @RequestParam(required = false) PageRequest pageRequest) {// page.size∈{10,20,50}
    // TODO only return items which are currently in stock
    // ❌ N+1 QUERY PROBLEM: 1 query for parents, 1 further / parent = N+1
    // ❌⭐️ you are filtering results AFTER☢️ pagination: SQL⇒20 items, 17❌ ⇒ 3 items > page 1 of 20, [20 items/page]
    // WHERE > SORT > LIMIT/OFFSET

    //🤔 productsOutOfStock~10K = stockApi.findAllInStock()
    //🤔 cache<product.id -> stock value> ttl=1min 😱😱😱😱😱😈😈

    //🤔✅ "IN-MEMORY PAGINATION"
    // let's fetch a 100, just in case. They ask for 50, right? Filter each of them, file by file or in batches, retrieving stocks per group of ID. If, by any chance, out of the 100 we are left with 49, we can retrieve another batch. See the stock for those. Repeat until we get the full page
    // ⇒ give me page 2⇒requires traversing page 1, gimme page 100=⇒😱
    // ⇒ [BRILLIANT] : have the user come back with the offset of the LAST item
    // we gave to know where to start in SQL results (OFFSET X)
    // ⭐️ inventoryApi.fetchStocksByBulks(product100Ids)

    // ⭐✅️#1❤️❤️❤️ Replicate in my data their information via an event
    // do this just before extracting a microservice
    // only save stuff that have stock. IS_ACTIVE=1|0 kept in sync with inventory stock ≥1
    // Product#inStock

    // ⭐️✅🤔#2 Join their VIEW
    // 🙁 module-scoped testing gets harder: catalog module tests will have to popukate inventory tables

    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
//        .filter(p->inventoryInternalApi.getStock(p.id()).orElse(1)>0)❌
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
