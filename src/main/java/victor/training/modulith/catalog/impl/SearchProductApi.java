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
public class SearchProductApi {
  private final ProductRepo productRepo;
  private final StockRepo stockRepo;

  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // 1) ⭐️ JOIN intre scheme prin VIEW-URI expuse de aia
    // TODO only return items in stock
    // OOME
    // List<> toateProduseleCuStockNeNul = productRepo.findAll().stream().filter(p -> stockRepo.findByProductId(p.id()).items()>0).toList();
    // in-memory join

    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
        .stream()
//        .filter(p ->stockRepo.findByProductId(p.id()).orElseThrow().items()>0) // 2ms x 1000 = 2s
//        .filter(p ->stockRestApi.get(p.id()).orElseThrow().items()>0) // 100ms x 50 = 5s
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
