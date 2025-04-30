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

  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> call(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items in stock
//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
//    return productRepo.searchJoinView("%" + name + "%", pageRequest) // A
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest) // B
        .stream()

//        .filter(p->inventoryInternalApi.getStockForProduct(p.id()) > 0)
        // TERRIBLE because:
        // - bad performance: lots of DB queries = N+1 queries problem
        // - bad UX: resulting page in UI may not be 20

//        .filter(p->listOf1MIdsOfAllProductIds.contains(p.id()))

        // find all product ids matching name (1M:))) and give them to inventory to check stock
        // Correct:
        // A) JOIN their VIEW 🤔🤨 -simplest, most pragmatic
        // B) REPLICATE their DATA (the stock count) - microservice-friendly

        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
