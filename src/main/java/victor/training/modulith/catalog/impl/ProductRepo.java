package victor.training.modulith.catalog.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
  List<Product> searchByNameLikeIgnoreCase(String namePart);
//  List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String namePart);
}
