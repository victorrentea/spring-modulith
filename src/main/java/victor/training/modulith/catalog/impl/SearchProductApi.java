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

  public record ProductSearchResult(long id, String name) {
  }
  private final StockRepo stockRepo;
  @GetMapping("catalog/search")
  public List<ProductSearchResult> execute(
      @RequestParam String name,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items in stock
    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%",
            pageRequest)// LIMIT OFFSET
        .stream()
        // RAU#1: performanta
        // 200ms x 50 item = 10 sec bataie de joc fata de client
//        .filter(p -> inventoryRestApi.inStock(p.id()))// REST API CALL

        // 20ms x 50 item = 1 sec bataie de joc fata de client
        // RAU#2: rupt incapsularea lui inventory
        // RAU#3: filtrezi dupa ce paginezi = GRESIT
        .filter(p -> stockRepo.findByProductId(p.id()).map(s -> s.items() > 0).orElse(false))
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
