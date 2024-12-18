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
public class SearchProductApi {
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventoryInternalApi;

  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO CR: only return items in stock TODO add more criteria
//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)

    // GOOD 2 (planning to split the DB, aka microservices):
    // replicate data from inventory to catalog: Stock#items -> Product#stock
    // HOW TO KEEP IT IN SYNC?
    //~ a persisted cache?


    // GOOD 1 (longer modulith): JOIN a VIEW from THEM. DON'T ABUSE!
    return productRepo.searchInStockByName("%" + name + "%", pageRequest)
        .stream()
        // BAD:
        //#1 KO incorrect to filter after pagination
        //#2 N queries(2-5ms)/REST calls(>>>10ms) coming up
//        .filter(p->inventoryInternalApi.getStock(p.id()) > 0)

        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
