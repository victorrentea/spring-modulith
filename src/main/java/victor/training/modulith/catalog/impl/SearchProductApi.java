package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
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
//    a) var allProductIdsInStock = inventoryRestApi.getAllInStock(); // too large list

    // b) get all products by name = OOME and then check which are in stock
    // TODO only return items in stock

    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
        .stream()
//  c)      .filter(prod-> inventoryApi.isInStock(prod.id())) // network call in the loop
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();

    // d)
    // get the ids of all the 20 products matching name and fetch their stock in bulks
    // Map<long, boolean> availability=inventoryApi.bulkGetAvailability(productIds);
    // remove those with false = 17
    // user sees 3 products on the first page (of 20 items suposedly) = bad UX

    // we should first FILTER then PAGINATE.

    // e) replicate state via Kafka or the like = ~~~inconsistencies .......
  }
}
