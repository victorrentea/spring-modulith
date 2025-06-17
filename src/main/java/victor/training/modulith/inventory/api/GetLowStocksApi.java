package victor.training.modulith.inventory.api;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.shared.api.catalog.CatalogInternalApi;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetLowStocksApi {
  private final StockRepo stockRepo;
  private final CatalogInternalApi catalogInternalApi;

  @GetMapping("stock/low")
  @Transactional
  public List<String> call() {
    var lowStocks = stockRepo.findStockByItemsLessThan(10);

    // from traversin an ILLEGAL JPA link
//    var productNames = lowStocks.stream().map(Stock::product).map(Product::name).toList();

    // to explicit query to the other side
//    var productIds = lowStocks.stream().map(Stock::productId).toList(); // N+1  query
//    List<String> productNames = catalogInternalApi.findProductByIds(productIds);
    // TODO return IDs of products low on stock
    //  + CR: get their names (speculate)
    return List.of();
  }
}
