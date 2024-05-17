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
    // TODO only return items in stock
//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
    return productRepo.searchInStockByName("%" + name + "%", pageRequest)
        //select a View

        .stream()
          //        .filter(p -> inventoryModule.getStock(p.id).stock() > 0)
          //   BAD performance = N+1 query & the page is no loger 20, but 7 items
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
