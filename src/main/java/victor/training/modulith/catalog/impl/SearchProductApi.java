package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.shared.api.inventory.InventoryModuleApi;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchProductApi {
  private final ProductRepo productRepo;
  private final InventoryModuleApi inventoryModuleApi;

  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items in stock
//    List<Long> iNStockIds1M = inventoryModuleApi.findAllIdsInStock(); // OOME prost
//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", iNStockIds, pageRequest)

    //A) JOIN pe modular monolith cu o instanta de DB unica

    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
        .stream()
//        .filter(p->inventoryModuleApi.inStock(p.id())) // groaznic pt ca: 1. e lent (20+1 query), 2. ai rupt pagina
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
