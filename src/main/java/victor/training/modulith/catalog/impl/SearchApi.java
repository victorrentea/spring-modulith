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

  public record ProductSearchCriteria(String name, String description) { }

  public record ProductSearchResult(long id, String name) {  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(
      @RequestParam ProductSearchCriteria criteria,
      @RequestParam(required = false) PageRequest pageRequest) {

    //List<> productIds=myRepo.search(
//    Map<productId, Integer> stockLevels = inventoryInternalApi.getStockForManyProductIds(productIds)

    // The user asked for 20. Just in case, let's get 60 out of my database. Go with the 60 overseas. Come back with those that are in stock, and hope they are at least 20.
    // In case we are very unlucky and out of the 60, only 3 were still in stock. Let's fetch 60 more in a while loop.
    // ☢️ in-memory join
    //Good luck going to page 10.

    // ✅ 1 JOIN THEIR VIEW

    // ✅ 2

    // TODO only return items which are currently in stock
    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
//        ❌.filter(p->inventoryInternalApi.getStock(p.id())>0)
        // 😡 SELECT in a loop (N+1 problem). problem if N is big (10,20,100)
        // 😡 SELECT FROM PRODUCT returned 20 items (1 page), but filter only leaves 1 item:
        //    page 1 of 100, 20 items/page, 1 item in your grid
        //    FILTER > SORT > PAGINATE
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }

}
