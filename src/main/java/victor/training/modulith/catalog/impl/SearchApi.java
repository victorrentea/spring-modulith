package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.InventoryInternalApi;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchApi {
  private final ProductRepo productRepo;
  private final InventoryInternalApi inventoryInternalApi;

  public record ProductSearchCriteria(String name, String description) { }

  public record ProductSearchResult(long id, String name) {  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(
      @RequestParam ProductSearchCriteria criteria,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return items which are currently in stock
//    var tateProdusele = productRepo.findAll(); // OOME💥


    // Solutia#1: Monolith💖 - Product JOIN StockView (-∑Reserved)
    // Solutia#2: curand EJECT la un Microserviciu - Product.inStock: boolean WHERE = 1, care sa fie actualizat cand se schimba stocu prin events

    // Solutia#3 pt @eduard: ia 50 (tu vrei 20) si daca nu raman 20 mai ia inca 80 x while(); pt total ia primele 1000. am 10+ pagini.

    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
//        .filter(p->inventoryInternalApi.getStock(p.id())>0)
        // Prost#1: N+1 query problem, n=1M, 20!
        //    ce fain ar fi sa sara o alarma in prod cand vede ca pe acelasi traceID, ai dat in PG ca animalu cu WHERE ID = ? x 50
        // Prost#2: prima pagina de 20 prod are doar 2 prod + 50 pages left.
        //    WHERE > SORT > PAGINATION
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();

  }
}
