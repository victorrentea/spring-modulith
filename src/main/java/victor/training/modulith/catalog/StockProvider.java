package victor.training.modulith.catalog;

public interface StockProvider {
  MyStockKnowWith3Fields getStock(Long productId);
}
