package victor.training.modulith.catalog.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewedProductRepo extends JpaRepository<ReviewedProduct, Long> {
  Optional<ReviewedProduct> findByProductId(long productId);
}
