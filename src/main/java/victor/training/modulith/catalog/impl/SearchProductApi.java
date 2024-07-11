package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchProductApi {
  private final ProductRepo productRepo;

  public record ProductSearchResult(long id, String name) {
  }
private final StockRepo stockRepo;
  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // 💡🚫 prefetch all the product ids in stock
    // List<Long> huge = stockRepo.findIdInStock(); // but fits in memory: 1M items
    // return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest, huge) // Bad news max 1000 x ? in SQL

    // 💡 #1 [Best for Modulith] JOIN with inventory.Stock - couples me to Inventory's internal

    // 💡 #2 [Best for Microservices] Caching :
    // - a copy of the IN_STOCK{productId,bool} in the catalog
    // - a boolean in the PRODUCT table

//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
//    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)
    return productRepo.searchInStockByName("%" + name + "%", pageRequest)
        .stream()
        // #1) N+1 queries = bad performance
        // #2) I retrieve eg a page of 20(=max), I filter and leave eg 7
//        .filter(product -> stockRepo.findByProductId(product.id()).map(Stock::items).orElse(0) > 0)
//        .filter(p->huge.contains(p.id())) // #2 is still here!
        .map(product -> new ProductSearchResult(product.id(), product.name()))
        .toList();
  }
}
