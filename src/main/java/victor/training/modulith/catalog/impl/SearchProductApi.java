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
import java.util.Map;

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


//    List<Product> products = productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest);
//    var productIds = products.stream().map(Product::id).toList();
//    Map<Long, Integer> stocks= inventoryInternalApi.getStockForProducts(productIds);
     // result: not always the requested 20 lines / page. Maybe fewer if out of stock

    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)
        .stream()
//        .filter(p -> inventoryInternalApi.getStockForProduct(p.id())>0)
        // N+1 queries problem;
        // a) now in-mem call to inventory -> N network calls to DB. 100 x 5ms = 500ms
        // b) now REST call to inventory -> N network calls to DB. 100 x ???? 50ms = 5000ms
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
