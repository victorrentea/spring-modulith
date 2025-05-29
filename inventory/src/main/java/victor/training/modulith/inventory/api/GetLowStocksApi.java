package victor.training.modulith.inventory.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetLowStocksApi {
  private final StockRepo stockRepo;
//  private final CatalogInternalApi catalogInternalApi;

  @GetMapping("stock/low")
//  @Transactional
  public List<String> call() {
    var lowStocks = stockRepo.findStockByItemsLessThan(10);

    // opt1: with link between the Domain Models
    // - DM coupling ❌❌
//    return lowStocks.stream().map(Stock::product).map(Product::name).toList();
    return lowStocks.stream().map(Stock::productName).toList();

    // migrate slowly to 🔽

    // opt2: call to catalog internal API
    // - performance: +1 SELECT
//    var productIds = lowStocks.stream()
//        .map(Stock::productId)
//        .toList();
//    return catalogInternalApi.getProductNames(productIds)
//        .stream().map(ProductInternalDto::name).toList();
  }
}
