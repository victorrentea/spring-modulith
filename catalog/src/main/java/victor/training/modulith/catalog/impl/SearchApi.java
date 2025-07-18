package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.shared.api.inventory.InventoryInternalApi;

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
      @RequestParam(required = false) PageRequest pageRequest) { // {pageSize:20,pageNo,sort} -> LIMIT / OFFSET
    // TODO CR only return items which are currently in stock

    // Fix #1 to avoid N queryies, fetch all stocks at once
    // Map<Long:productId, Integer:stock> stocks = inventoryapi.getStocks(List<Long:productId>) --> SELECT ... WHERE ID IN(?,?..)

    // Fix#1+#2:
    // a) join their view (SQL+monolith forever)
    // b) replicate their data you need (via an event)
    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()// 20
//        .filter(r->inventoryInternalApi.getStock(r.id())>0)
        // #1) "N+1 QUERY problem": calling network in a loop = performance massacre
        // #2) 15 items left from 20 = BAD UX
        // let's take 40 just in case. and if after filtering we're left < 20, take 40 more (for)
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
