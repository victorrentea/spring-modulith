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
    // OOME
    // List<> toateProduseleCuStockNeNul = productRepo.findAll().stream().filter(p -> stockRepo.findByProductId(p.id()).items()>0).toList();
    // in-memory join
    // TODO only return items in stock
    List<ProductSearchResult> list = productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)
        .stream()
//        .filter(p ->stockRepo.findByProductId(p.id()).orElseThrow().items()>0) // 2ms x 1000 = 2s
//        .filter(p ->stockRestApi.get(p.id()).orElseThrow().items()>0) // 100ms x 50 = 5s
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
//    var selectedProductIds = list.stream().map(ProductSearchResult::id).toList();
//    // 20
//    List<Long> iduriDeProduseInStock = inventoryApi.damiDacaSuntInStockPentruProduseleAstea(selectedProductIds);
//    list.removeIf(p -> !iduriDeProduseInStock.contains(p.id()));
    // <= 20, poate 0 ca nici unu din pagina gasita dupa nume nu s-a dovedit a fi in stock
    return list;
  }
}
