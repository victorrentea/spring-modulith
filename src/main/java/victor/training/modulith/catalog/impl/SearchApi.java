package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;

import java.util.List;
import java.util.Optional;

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
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items in stock (there is a Stock{productId}.items > 0)

    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
        .stream()
        .filter(product -> stockRepo.findByProductId(product.id()).get().items() > 0)
        // BAD because
        // 1 screw encapsulation
        // 2 bad performance (N+1) queries: repetead in a loop
        // 3 actual page returned might not be 20 (as requested)
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
