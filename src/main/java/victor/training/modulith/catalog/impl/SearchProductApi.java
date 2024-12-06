package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.model.Stock;
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

    // even worse performance = runtime: 10 min: conn/thread starvation
//    List<Product> allProducts = productRepo.findAll()
//        .parallelStream()// 100 threads ftw -> DoS
//        .filter(p->stockApi.findByProductId(p.id()).map(Stock::items).orElse(0) > 0)
//        .toList();

    // too good!! = OOM
//    List<Stock> allStock = stockRepo.findAll();
//    List<Product> allProducts = productRepo.findAll(); // 1GB data
    // then, in-memory

    // get all Ids of  ALL products matching name (100k) and fetchAllInventoryForThatIdList(listIds)

    // Keep ids of all products in stock in a HashSet<Long> in memory.size 1M x8 = 12MB - @Kris
    // @KafkaListener updates it from a compact topic in Kafka, get all ids of products in stock
    // a compact topic is a Kafka topic that only keeps the latest value for each key ~ table
    // Country list, FEX,  reference data

    // #1 GOOD: if you plan to keep in the same process (modular monolith) with inventory, join their VIEW


    // #2 Events:

    // TODO only return items in stock
//    return productRepo.searchByNameLikeIgnoreCase("%" + name + "%", pageRequest)
    return productRepo.searchByNameLikeIgnoreCaseAndInStockTrue("%" + name + "%", pageRequest)
        .stream()
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
