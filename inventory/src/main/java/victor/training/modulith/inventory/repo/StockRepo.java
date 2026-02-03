package victor.training.modulith.inventory.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.modulith.inventory.model.Stock;

import java.util.List;
import java.util.Optional;

public interface StockRepo extends JpaRepository<Stock, Long> {
  Optional<Stock> findByProductId(long productId);
  List<Stock> findStockByItemsLessThan(int count);
}
