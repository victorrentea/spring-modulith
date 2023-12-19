package victor.training.modulith.catalog.impl;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
  List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String namePart);

//  @Lock(LockModeType.PESSIMISTIC_WRITE) // SELECT ... FOR UPDATE;
//  // - transaction-scoped row locking => may bottleneck in DB / starve the connection pool = pessimistic locking
//  @Override
//  Optional<Product> findById(Long aLong);
}
