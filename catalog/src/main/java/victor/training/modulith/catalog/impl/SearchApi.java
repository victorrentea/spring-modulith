package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.shared.api.inventory.InventoryInternalApi;

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
    // Fix#1: join their view (modulith/sql)
    // Fix BAD: call their (REST) API = WRONG
    // Fix#2: replicated in my DB what i need from THEM (microservice)
    //

    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
        // 20 items (1 page) returned
        .filter(p->inventoryInternalApi.findStockByProductId(p.id()).orElseThrow()>0)
        // 5 items left => bad UX: expected 20, sees 5
        // Bad because #1: performance: the infamous network-call-in-a-loop
        //   Fix: -> batch API: -19 api calls x 20ms = -400ms
        // Bad because #2: availability--: if they are down -> I'm down.
        // Bad because #3: pagination is ruined!
        // Hack: dont-fetch 40 at first and if after filter <20 areleft, fetch 30 more until we fill the page


        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
