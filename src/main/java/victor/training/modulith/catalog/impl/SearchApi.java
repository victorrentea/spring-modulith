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
  private final StockRepo stockRepo;
  private final InventoryInternalApi inventoryInternalApi;

  public record ProductSearchCriteria(String name, String description) { }

  public record ProductSearchResult(long id, String name) {  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(
      @RequestParam ProductSearchCriteria criteria,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO we should only return products which are currently in stock
//    var allStocks = stockRepo.findAll(); // 1GB in RAM -> OOME
    // hey inventory, are these products in stock?
//    return productRepo.search(criteria.name, criteria.description, pageRequest)
    // B) VIEW

    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
//        .filter(p->inventoryInternalApi.getStockByProduct(p.id())>0) // TODO inefficient, N+1 queries
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();


    // ❌
    // search in CATALOG.PRODUCTS > List<Products>
    // Set<Long> inStockIds=inventoryIApi.whichAreInStock(productIds);
    // retain in my list those which are In stock.
    // screws pagination. of 20 / page -> 18 left
    // if that ever happens, get 40 more while

    //⚠️ WE NEED TO FILTER BEFORE PAGINATION/SORTING!

    // a) ✅ REPLICATE😱 data iff distributed arch (before IGNITION🚀)
    // b) ✅ DB VIEW over Inventory Data until then, in a Modular Monolith

  }
}
