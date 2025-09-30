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
  public static final int INFINTE_STOCK = Integer.MAX_VALUE;
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventoryInternalApi;

  public record ProductSearchCriteria(String name, String description) { }

  public record ProductSearchResult(long id, String name) {  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(
      @RequestParam ProductSearchCriteria criteria,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items which are currently in stock
    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
//        .filter(p-> inventoryInternalApi.getStockByProduct(p.id())
//                        .orElse(INFINTE_STOCK) > 0)
        //BAD#1: N+1 query problem: SELECT in a loop (max 20)
        //BAD#2: bad UX it doesn't return a proper page.
        //  User requested a page of 20, you found 20 in DB, but you returned 20,18,5,0
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
