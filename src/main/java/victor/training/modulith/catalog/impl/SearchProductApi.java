package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchProductApi {
  private final ProductRepo productRepo;

  public record ProductSearchResult(long id, String name) {
  }
  private final InventoryInternalApi inventoryInternalApi;
  // CustomerDoor=CustomerInternalApi
  // CustomerKnob=CustomerInternalDto

  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // prefetch the data from inventory
    List<Long> allProductIdsInStock = inventoryInternalApi.getAllProductIdsInStock();
    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
        .stream()
        .filter(p -> allProductIdsInStock.contains(p.id()))
        // 3) you screwed up the page size
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
