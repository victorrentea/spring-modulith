package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchProductRest {
  private final ProductRepo productRepo;

  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(@RequestParam String name) {
    // TODO 2  CR: search should only display items in stock
    // 1: SQL/dsl: PRODUCT JOIN STOCK (why do I feel guilty?!) <<<<
    // 3: filter by a boolean 'inStock' that we keep in sync with inventory.
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue(name)
        .stream()
//      2:  .filter() // WRONG 1: causes N x SELECT in inventory; 2: how about pagination?
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
