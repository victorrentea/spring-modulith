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
    List<ProductSearchResult> all = productRepo.searchByNameLikeIgnoreCaseAndInStockTrue(name).stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
    // a)IN-MEMORY JOIN  memory hit!!!
//    for (ProductSearchResult result : all) {
//      int stock = stock.getItems(product.id);
//      if (stock > 0) {
//        result.setInStock(true);
//      }
//    }

    // b) denormalization: keep in Product the inStock
    // b1) inventory module updates the inStock field in Product
    // b2) events from inventory module update the inStock field in Product

    // c) in my search query JPQL I will JOIN StockViewEntity mapped to INVENTORY.STOCK_VIEW view
    // and filter by stock > 0
    return all;
  }
}
