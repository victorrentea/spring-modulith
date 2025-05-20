package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    // TODO only return items currently in stock

//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)

    // ✅ OK for a long-lived Monolith
//    return productRepo.searchJoinView("%" + name + "%", pageRequest)

    // ✅ OK replicating data from inventory
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)
        .stream()
//        .filter(p->stockRepo.findByProductId(p.id()).orElseThrow().items() >0) // N+1 quyery, screwed page size, broke encapsulation
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
