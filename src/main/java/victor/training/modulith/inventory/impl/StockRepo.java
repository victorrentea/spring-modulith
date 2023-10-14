package victor.training.modulith.inventory.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import victor.training.modulith.shared.ProductId;

import java.util.Optional;

import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface StockRepo extends JpaRepository<Stock, Long> {
  @Lock(PESSIMISTIC_WRITE) // todo test
  Optional<Stock> findByProductId(ProductId productId);
}
