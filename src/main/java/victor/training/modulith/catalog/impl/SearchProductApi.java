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
    // TODO 2 CR: search should only display items in stock, ofc
    // I have 2.5 milion products, filter out of stock=> 1.5 million LIMIT
    return productRepo.searchByNameContainsAndInStockTrue(name).stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }

  // GEt me an sql to select the procucts containing some name part that have stock > 10
  // sql = "select *
  // from procuct.product
  // JOIN stock.stock
  // where name like ? and s.stock > 10"

  // option1: JOIN across schemas :: illegal > creeate VIEW. CHEATING!!!!
  // option2: move the search to an orchestrator module that is migrated data
  // option3: cache product names in stock
  //
}


