package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchProductApi {
  private final ProductRepo productRepo;

  public record ProductSearchResult(long id, String name) {
  }
//  private final StockRepo stockRepo;

  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items in stock
//    List<Stock> all10000000 = stockRepo.findAll();
    return productRepo.searchInStockByName("%" + name + "%", pageRequest)
        .stream()
//        .filter(p -> stockRepo.findByProductId(p.id()).orElseThrow().items() > 0) // Perf hit: N+1 queries = 20 + 1
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
