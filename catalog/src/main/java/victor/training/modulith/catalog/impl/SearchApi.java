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
      @RequestParam(required = false) PageRequest pageRequest) { // db-level pagina tion
    // ORDER BY + LIMIT + OFFSET in SQL

    // TODO only return items which are currently in stock
    // ❌) THE WORST
    // A) ⭐️catalog.PRODUCT JOIN inventory.STOCK in SQL [in a VIEW ❤️]
    //  👍 best for Long-Modulith
    //  🙁 when testing catalog module I will have to create+populate tables of inventory=🤢

    // B) ⭐️Replicate stock level from inventory/ -> catalog/Product.stock
    //    i) ⭐️via @EventListener(in-mem)✅/@KafkaListener🦄
    //      for an event fired by Inventory module/microservice
    //    ii) WebHook: PUSH via API: inventory --REST call-> catalog: error-prone, retry, timesout = Operation coupling☢️
    //    iii) PULL via API: catalog polls --GET-> inventory "what's new since 5 sec ago": inefficient, costly,
    //    iv)❌ persistent network connection websockets/sse 😱
    //  👍 best if planning to split a Microservice out

    // ❌
    // Map<productId:int, stock:int> map = inventoryApi.findAllStocks(); //=> OOME💥

    // ❌
    // var results = productRepo.search(criteria.name, criteria.description, pageRequest)
    // Map<productId:int, stock:int> stocksMap = inventoryApi.findStocks(results.map{it.id});
    // return results.filter{stocksMap.get(it.id)>0}
    // => pagination is ruined: not 20 per page but perhaps 15,12...0?; WHERE > LIMIT >OFFSET > filter=WHERE

    // ❌ HACK: in-mem pagination
    //  if the user asked for 20, let's bring 40 from my database.
    //  Fetch all the 40 from stock, and if I am left with less than 20, repeat
//    return productRepo.search(criteria.name, criteria.description, pageRequest)
    return productRepo.search2(criteria.name, criteria.description, pageRequest)
        .stream()
//        ❌❌.filter(product -> restTemplate.GET localhost stock public REST) // = Performance Massacre
//        ❌.filter(product -> inventoryInternalApi.getStockByProduct(product.id()).orElse(1) >=0) // N+1
//        .filter(product -> map.get(product.id)>0)
        .map(e -> new ProductSearchResult(e.id(), e.name()))
        .toList();
  }
}
