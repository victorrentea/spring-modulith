package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
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
    // TODO only return items in stock
    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
        .stream()
        .filter(product -> fetchStock(product.id()) > 0)
        //#1: only 1 items left of the intended page of 20  => BAD UX
        //#2: for() { api.call } => 20 calls to inventory => BAD PERFORMANCE
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }

  private int fetchStock(Long id) {
    return new RestTemplate().getForObject("http://localhost:8080/inventory/stock/" + id, Integer.class);
//    return inventoryInternalApi.getStock(id); // recommended if inventory in inside the same deployable
  }
}
