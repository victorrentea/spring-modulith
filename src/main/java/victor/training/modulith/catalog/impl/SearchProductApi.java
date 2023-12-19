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
    return productRepo.searchByNameLikeIgnoreCase(name).stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
