package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

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
    // TODO only return items in stock
//    ;productRepo.findAll().stream().map(Product::id).toList();// cant reach into their DB
    // Cons: operational-coupling to inventory-service: if that's down, search is down
    // Cons: 10M Long = 80MB traffic + memory => OOME?!
    // Cons: filtering after pagination = bad semantics= bugs
//    Set<Long> allProductIdsInStock = inventoryApi.findAllProductIdsInStock();

    // WHERE ID IN (?,?,?,....10M?) <max 1000 x ? /query > syntax error in SQL + TO MUCH DATA
//    return productRepo.searchByNameLikeIgnoreCaseAndIdIn("%" + name + "%", allProductIdsInStock, pageRequest)

//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)
        .stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
