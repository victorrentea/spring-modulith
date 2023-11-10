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
  public List<ProductSearchResult> search(@RequestParam String name) { // or @RequestBody SearchCriteria
    // SELECT * from Product p
    // JOIN Stock s ON p.id = s.productId
    // WHERE p.name LIKE ?1 AND s.items > 0
    // VIEW? - nu e bine sa faci JOIN intre tabele din module diferite

    // TODO 2 return only items in stock
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue(name)
        .stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
