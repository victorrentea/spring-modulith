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
//  private final InventoryInternalApi inventoryInternalApi;
  // CustomerDoor=CustomerInternalApi
  // CustomerKnob=CustomerInternalDto

  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // REAL SOLUTION #1) REPLICATION = microservice way
    // REAL SOLUTION #2) JOIN = monolith way
    // > .. with STOCK (their main, 70-columsn table)
    // > .. with STOCK_VIEW they crafter with love for US
    //    !! LAST RESORT: ONLY USE THIS SOLUTION WHEN YOU CANNOT call the API from JAVA
    //    WHEN you HAVE to JOIN in =
    //    - a paginated search (filtering before pagination)
    //    - export (massive data) AFTER YOU BENCHMARK THE BENEFIT
    // r3eject the PR with a JOINING a VIEW unless it is acoompanied by a 2 paragraph description WHY!!!!!!?!

//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest) // can't pass 1M ? into a queryt
    return productRepo.searchInStockByName("%" + name + "%", pageRequest)
        .stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
