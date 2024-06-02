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
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items in stock
    //a) JOIN intre module
    //b) API call intre serviciile
    //    List<Long> allProductIdsInStock1M =.... pt fiecare fac SEELCT in PRODUCT la mine
    //    productRepo.findAll().filter(

    // c) migrezi datele de stock in catalog printrun Kafka event emis de inventory StockUpdatedEvent
    // sau OutOfStockEvent/BackInStockEvent
    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
        .stream()
//        .filter(p->stockApi.isInStock(p.id()) // N network calls + strici dimens paginii
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
