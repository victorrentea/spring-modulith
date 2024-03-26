package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
  public List<ProductSearchResult> search(
      @RequestParam String name,
      @RequestParam PageRequest pageRequest) {
    // TODO fix BUG: only return items that are in stock
    // #1 In-memory join:
    //  find IDs of all products in stock in inventory
    //  then pass those IDs to a query to the PRODUCT TABLE WHERE ID IN (?,...?)
    //    potential OutOfMemoryError

    // #2 JOIN in DB CATALOG.PRODUCT
    // WHERE (EXISTS SELECT * FROM INVENTORY.STOCK_VIEW (a "contract" of the Inventory Module)
    //    WHERE STOCK.PRODUCT_ID = PRODUCT.ID
    //    AND  STOCK.ITEMS > 0)

    // #3 replicate the interesting data to my schema via events
    return productRepo.searchByNameLikeIgnoreCaseAndAvailable(name, pageRequest)
        .stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
