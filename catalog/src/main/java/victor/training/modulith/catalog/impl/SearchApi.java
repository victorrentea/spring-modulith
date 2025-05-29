package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.shared.api.inventory.InventoryInternalApi;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchApi {
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventory;
  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> call(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items in stock => SearchE2ETest

    // 2 correct options:
    // B Replicate state Product#inStock = microservice-friendly
    // kept in sync via an event (intially in-mem, eventually broKer)
//    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)

    // A Join a VIEW crafted and maintained with 💖 by Inventory Team for me to join in EXCEPTIONAL CASES
    // WARNING: don't ABUSE. only for performance.
    return productRepo.searchJoinView("%" + name + "%", pageRequest)
        .stream()
//        .filter(p->inventory.getStockByProductId(p.id())>0)
        // BAD because:
        // 1 Performance hit: N+1 qeuery (network call in a loop)
         // inventory.findStockFroProductIds(productIds) -> SELECT WHERE ID IN (?,?...)
        // 2 Bad UX: user wants 20 lines / page, but they might see 19 or 5 or 0 :)) if they are filtered
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
