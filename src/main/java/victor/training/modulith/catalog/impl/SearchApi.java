package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.repo.StockRepo;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchApi {
  private final ProductRepo productRepo;
  private final StockRepo stockRepo;

  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> call(
      @RequestParam String name,
      @RequestParam String category,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items in stock (there is a Stock{productId}.items > 0)

//    List<Long> allProductsInStock = stockApi.findAllProductIdsInStock(); // 10 M items too many BAD
//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)


    // A) for staying in a monolith
    // I NEED TO JOIN CATALOG.PRODUCT with INVENTORY.STOCK => query will break when inventory team alters their table
    // CREATE A VIEW JOINING the two tables. - who owns it
    // CREATE A VIEW offerd with ❤️ by INVENTORY team
//    return productRepo.searchJoinView("%" + name + "%", pageRequest)

    // B) microservices-friendly
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)
        .stream()
//        .filter(product -> stockRepo.findByProductId(product.id()).get().items() > 0)
        // BAD because
        // 1 screw encapsulation
        // 2 bad performance (N+1) queries: repetead in a loop
        // 3 actual page returned might not be 20 (as requested) -> use JOIN to move the filtering before pagination
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
