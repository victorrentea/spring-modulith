package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.repo.StockRepo;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchProductApi {
  private final ProductRepo productRepo;

  public record ProductSearchResult(long id, String name) {
  }
  private final StockRepo stockRepo;

  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items in stock (>=1)
//    List<Product> productsMatchingName = productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest);
    List<Product> productsMatchingName = productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest);
//    stockRepo.
    //acceptable if ids.size<1000
//    Map<Long,Integer> stockLevels = stockGRPCApi.getStockForProductIds(allIds); // less network traffic
    return productsMatchingName
        .stream()
//        .filter(p -> stockRepo.findByProductId(p.id()).orElseThrow().items() > 0)
//        .filter(p -> stockGRPCApi.findByProductId(p.id()).orElseThrow().items() > 0)
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
