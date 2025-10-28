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
public class SearchApi {
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventoryInternalApi;

  public record ProductSearchCriteria(String name, String description) { }

  public record ProductSearchResult(long id, String name) {  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(
      @RequestParam ProductSearchCriteria criteria,
      @RequestParam(required = false) PageRequest pageRequest) {
//    var allProductsInStock = select=>OOME
//    var allProductsMatchingNAmeAndDescr= LIMIT OFFSET

    // Option 1: JOIN smth from INVENTORY
    //

//    Map<Long,Integer> stockForProducts = inventoryInternalApi.getStockForProduct(productIdsMatchingCriteria);

    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
//        .filter(product ->stockRepo.findByProductId())
        // ❌1) Invading inventory module (breaking their encapsulation)
//        .filter(product ->inventoryInternalApi.getStockForProduct(product.id()))
        // ❌2) Performance disaster: N+1 SELECT
        // 20 products (page 1/102)----
//        .filter(p->stockForProducts.get(p.id())>0)
        // 1 products BAD🙈 UX . user: 🤔
        // ❌3)pagination violation: WHERE > SORT > PAGINATE
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
