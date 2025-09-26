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
    // initial
//    return productRepo.search(criteria.name, criteria.description, pageRequest)
//        .filter(p -> inventoryInternalApi.getStockByProduct(p.id()).orElse(0) > 0)
        // shit💩 because:
        // 1 performance hit: N+1 query
        // 2 UX hit: pages are no long 20 (as user requested). could be 20, 15, 1, 0
    // Fix#1:
    // Map<Long:productId, Int:stock> x = inventoryInternalApi.getStock(productIdList); // 1 call -> WHERE ID IN (?,?,..)

    // Fix for #1 + #2: JOIN THEIR TABLE = couples you to THEIR internals => freezes their persistence model => their DB turns into their enemy
        // PICK this👆 if you plan to stay in a modulith for long
//    return productRepo.searchJoiningAView(criteria.name, criteria.description, pageRequest)

    // Fix for #1 + #2: replicate data from THEM
    // How do I keep my state in sync with THEM?
    // a) in-process events 💖💖💖, for tomorrow: external messages broker: Kafka in microservices
    // b) pollers pulling from them or pushing to me🚫: cloud run
    // c) they call me when they update their stock🚫: WebHook
    return productRepo.searchByMyAttr(criteria.name, criteria.description, pageRequest)
        .stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
