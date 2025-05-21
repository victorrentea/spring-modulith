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
public class SearchApi {
  private final ProductRepo productRepo;

  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> call(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items in stock => SearchE2ETest
//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)

    // #1 if you plan to stay in monolith for long
//    return productRepo.searchJoinView("%" + name + "%", pageRequest)

// #2 suitable if you plan to move either of the 2 modules as a microservice soon

    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)
        .stream()
//        .filter(p -> stockRepo.findByProductId(p.id()).orElseThrow().items() > 0)
        // - PERFORMANCE HIT: N+1 query
        // - 500 instead of not found due to .orElseThrow
        // - broke encapsulation
        // - if the user requested a page of 20, they might receive 5 (15 filtered out)
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
