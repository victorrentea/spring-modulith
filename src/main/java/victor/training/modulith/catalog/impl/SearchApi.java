package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SearchApi {
  private final ProductRepo productRepo;

  public record ProductSearchCriteria(String name, String description) { }

  public record ProductSearchResult(long id, String name) {  }

  @GetMapping("catalog/search")
  public List<ProductSearchResult> search(
      @RequestParam ProductSearchCriteria criteria,
      @RequestParam(required = false) PageRequest pageRequest) {
    // TODO only return products which are currently in stock
    // ✅ 1 JOIN their VIEW (controlled, narrow, contain biz logic)
    //   🙁 in your module integration tests you'll still need THEIR TABLE filled, can't @Mock the view
    //   🙁 can be abused ☢️: other modules would prefer to SELECT from view over calling their API = WRONG!

    // ✅ 2 Data Replication (Cleaner, Microservice-ready not sharing DB ~Bezos Mandate) via:
    //  - Events❤️ (Rabbit/Kafka/ActiveMQ)
    //  - CDC with Debezium.io auto-sending kafka messages on tx commit in green
    //  - PG cross-instance table replication
    //  - BAD: I pull via API call "what's new since 5m ago"🤢
    //  - WORSE: inventory PUSHes (calls my API)8

//    Map<Long:productId, Integer:stock> allStocks = inventoryApi.getALlStocks()
    // ☢️ In-memory join (desperate solution):
    // They ask for a page of 20. You pick from your product 40.
    // Go with the 40 IDs to stock. Get the stock level filter,
    // and if by any chance you are left with less than 20, you go
    // fetch 40 more and repeat

    return productRepo.search(criteria.name, criteria.description, pageRequest)
        .stream()
//        .filter(p-> restApi call to inventory) ❌❌❌❌❌
        // 1) NETWORK CALL instead of an in-mem method call to inventoryInternalApi
        // 2) PERFORMANCE MASSACRE: N+1 QUERY PROBLEM: for { db.call }
        // 3) UX BUG: uneven pages: first query brought 20 (a page),  left after filter
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
