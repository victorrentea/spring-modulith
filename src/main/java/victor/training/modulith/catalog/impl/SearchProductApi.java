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
  }
}
// restrict the  products returned to only those with stock > 0 in inventory module.
// 1) In-memory join
// - fetch all matching product and then check each in inventory module with a bulk fetch
// - get from inventory all the product IDs that are in stock (MIllions)
// STOP when facing in-memory pagination!!

// 2) DB join: what if I JOIN my CATALOG.PRODUCT JOIN INVENTORY.PRODUCT_STOCK = cheating DONT

// 3) replicate data via events from inventory

// 4) modules publish an "OUTPUT_VIEW"; the inventory module exposes officially a
// VIEW with 3 columns for other modules to JOIN.
// that view is part of the INVENTORY API contract
// then the catalog JOINS that view

// 5) Elastic Search / search engine updated via events from inventory and catalog