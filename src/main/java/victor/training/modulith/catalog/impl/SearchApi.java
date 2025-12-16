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

    // list of my products SELECT WHERE name,description

//    List<id> all2MIdsInStock=inventoryInternalApi.findAllIdsInStock();😱

    // 1) 👍JOINing a VIEW of another module if you plan to remain monolithic
    // 🙁 it will complicate module-scoped tests; catalog tests will have to populate inventory table😱

    // 2) 👍 REPLICATE the data I need from THEM = microservice-ready solution
    // - events in-memory (tomorrow: Kafka)
    // - job every 5?😱 sec downloading all😱 ❌


    // ❌ search without pagination = last resort;
    // List<Long> allIds = productRepo.search(criteria); 2M x 8b = 16MB
    // inventoryApi.getStockFor(allIds) 16MB JSON payload coming up
    // send 300 ids, expect back stock. what are the chances that 281 are out of stock?!!!?!?
    // if that happens, send 300 more.


    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
//        .filter(product -> inventoryInternalApi.getStockByProduct(product.id()) > 0)
        // ❌ 1 SELECT / product = FAMOUS N+1 QUERY PROBLEM ⭐️⭐️⭐️ ~> Fix:getStockByProduct(List<Long> productIdList)
            // => .filter(product -> map.get(product.id())>0)

        // ❌ pagination is screwed
        // ifList returned by repo.search contains 20 (a page) LIMIT 20 OFFSET 1,
            // after filter we are left with less eg 15
        // ⭐️ in searching we do WHERE > SORT > PAGINATE
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
