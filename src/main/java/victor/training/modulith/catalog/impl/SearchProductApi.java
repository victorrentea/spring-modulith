package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.inventory.repo.StockRepo;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchProductApi {
  private final ProductRepo productRepo;
  private final StockRepo stockRepo;
  private final InventoryInternalApi inventoryInternalApi;

  public record ProductSearchResult(long id, String name) {
  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
//    productRepo.findAll().stream() // risk of OutOfMemory #1
//    List<Long> ids = inventoryInternalApi.findAllProductIdsInStock();// 1M x 8b = 8M , memorie si DB rupta
//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
    return productRepo.searchInStockByName("%" + name + "%", pageRequest)
        .stream()

//        .filter(p-> stockRepo.findByProductId(p.id()).get().items()>0) #2
//        .filter(p-> inventoryInternalApi.isInStock(p.id())) #2bis
        // te da afara pt 2 motive:
        // 1) performanta = faimosul N+1 queries faci in for{ SELECT esti BOU
        // 2) strici pagina din baza. Nu mai arati 20/pag ci cate raman
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
    //daca astea ar fi microservicii, ai 2 solutii:
    // a) ElasticSearch
    // b)
  }
}
