package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.inventory.repo.StockRepo;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchProductApi {
  private final ProductRepo productRepo;

  public record ProductSearchResult(long id, String name) {
  }
  private final InventoryInternalApi inventoryInternalApi;
  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // a) JOIN with stock!? no, with a VIEW the inventory team conscoiusly expose to others

    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
        .stream()
        .filter(p -> inventoryInternalApi.getStockForProduct(p.id())>0)
        // N+1 queries problem;
        // a) now in-mem call to inventory -> N network calls to DB. 100 x 5ms = 500ms
        // b) now REST call to inventory -> N network calls to DB. 100 x ???? 50ms = 5000ms
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
