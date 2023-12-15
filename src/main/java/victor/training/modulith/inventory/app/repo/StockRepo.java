package victor.training.modulith.inventory.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.modulith.inventory.app.entity.Stock;

import java.util.Optional;

public interface StockRepo extends JpaRepository<Stock, Long> {
  Optional<Stock> findByProductId(long productId);
}
