package victor.training.modulith.inventory.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepo extends JpaRepository<Stock, Long> {
  Optional<Stock> findByProductId(long productId);
}
