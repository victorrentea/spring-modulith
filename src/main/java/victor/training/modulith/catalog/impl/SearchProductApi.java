package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchProductApi {
  private final ProductRepo productRepo;

  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(@RequestParam String name) {
    // TODO search should only return items in stock
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue(name).stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
    // challenge: restrict result set to only items that have Stock.items>0 in 'inventory' module
    // 1 JOIN in DB across 2 modules schema -........ coupling to THEIR TABLE > lose control on the persisistence model
    // 2 aggregator JOINing in memory 10k products with their stock from 'inventory'
    // 3 Page object (limit to max 20 products), but then filtering products out of stock leaves you with 1 product
    // 4 RanOutOfStockEvent, ItemBackInStockEvent
  }
}
