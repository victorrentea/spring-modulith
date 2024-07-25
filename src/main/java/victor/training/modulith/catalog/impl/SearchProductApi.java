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
//  private final InventoryInternalApi inventoryInternalApi;
  // CustomerDoor=CustomerInternalApi
  // CustomerKnob=CustomerInternalDto

  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // REAL SOLUTION #1) REPLICATION = microservice way

    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest) // can't pass 1M ? into a queryt
        .stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
