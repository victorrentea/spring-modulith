package victor.training.modulith.catalog.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
  List<Product> searchByNameContainsAndInStockTrue(String namePart);
}
