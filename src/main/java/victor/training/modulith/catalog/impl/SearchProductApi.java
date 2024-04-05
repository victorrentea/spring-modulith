package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryModule;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchProductApi {
  private final ProductRepo productRepo;
  private final InventoryModule inventoryModule;

  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(
      @RequestParam String name,
      @RequestParam PageRequest pageRequest) {
    // TODO only return items in stock
    return productRepo.searchByNameLikeIgnoreCase(name, pageRequest)
        .stream()
       .filter(p -> inventoryModule.getStockByProduct(p.id()) > 0)
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
