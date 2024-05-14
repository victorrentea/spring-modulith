package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryModuleApi;

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

//    List<Long> allIdsInStock= inventoryModuleApi.getAllProductIdsInStock(); // huge list
//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest/*, allIdsInStock*/)

    // WORKS #2: JOIN A VIEW FROM INVENTORY

    // WORKS #1:
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest/*, allIdsInStock*/)
        .stream()
//        .filter(e -> allIdsInStock.contains(e.id())) // may remove elements from the page => bad UX
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
