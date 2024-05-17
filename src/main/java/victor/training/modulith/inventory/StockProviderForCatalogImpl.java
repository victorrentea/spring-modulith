package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.catalog.MyStockKnowWith3Fields;
import victor.training.modulith.catalog.StockProvider;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockProviderForCatalogImpl implements StockProvider {
  private final StockRepo stockRepo;
  @Override
  public MyStockKnowWith3Fields getStock(Long productId) {
    Stock stock = stockRepo.findByProductId(productId).orElseThrow();
    return new MyStockKnowWith3Fields(stock.productId(), stock.items());
  }
}
