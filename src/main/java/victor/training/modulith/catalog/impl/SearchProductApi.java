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

//    oneMil inventoryModule.getAllInStock() -> 1M id produce
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)
        .stream()
//        .filter(p -> inventoryModule.isInStock(p.id())) // 20 items / page -> 17 items
        .map(p -> new ProductSearchResult(p.id(), p.name()))
        .toList();
  }
}
