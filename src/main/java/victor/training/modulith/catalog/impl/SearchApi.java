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
    // #1 join inventory schema
//    return productRepo.search(criteria.name, criteria.description, pageRequest)

    // #2 replicate(?!desync) them via events, if planning to microservice soon
    // save their data (stock level) in my schema => +1 table/column in Product
    // listen to their events and update this column


    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
//        .filter(p->inventoryInternalApi.getStock(p.id())>0) // inefficient, kills pagination
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
