package victor.training.modulith.inventory.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface StockRepo extends JpaRepository<Stock, Long> {
  Optional<Stock> findByProductId(long productId);
}
