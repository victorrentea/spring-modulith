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
    // TODO search should only return items in stock > 0
    // idea1: IN-MEMORY JOIN: call inventory for EVERY item matching the criteria, or in bulks.
    // "e" is the product name -> 100k matches => 100 x 1000 calls to inventory
    // List<>100k elements.filter() => OutOfMemory

    // idea2: cheat and JOIN their table. doable in a single database. STRONGLY CONSISTENT
    // SELECT p.*
    // FROM CATALOG.PRODUCT p
    // JOIN INVENTORY.STOCK_OUT_VIEW s ON p.ID = s.PRODUCT_ID
    // WHERE p.NAME LIKE '%name%' AND s.ITEMS > 0

    // idea3: push events to ElasticSearch to enable also full-text search (external search engine)
    // EVENTUAL CONSISTENCY ⚠️⚠️⚠️⚠️⚠️ there is a delay between the product being sold and the search engine being updated

    // idea4: events to maintain the stock levels in catalog. = STRONGLY CONSISTENT
    // ProductSoldEvent(productId, items) -> product.inStock -= items
    // StockChangedEvent(productId, newStock) -> product.inStock = newStock
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue(name).stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
