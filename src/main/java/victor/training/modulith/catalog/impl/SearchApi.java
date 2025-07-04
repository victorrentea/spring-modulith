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
public class SearchApi {
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventoryInternalApi;

  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> call(
      @RequestParam String name, // Description, color, size, price, ... 10 more
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items that are currently in stock
//    var list10Mitems = inventoryInternalApi.getAllProductsWithStock()
//    Map<long,int:stock> manyStocks = inventoryInternalApi.getManyStocks(list50ProductIds); // WHERE ID IN (?,...?)

//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)
        .stream()
//        .filter(p->stockRepo.findByProductId(p.id()).orElseThrow().items()>0)
//        .filter(p->inventoryInternalApi.getStockForProduct(p.id())>0) // TOO LATE: has to happen before LIMIT / OFFSET in SQL
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
