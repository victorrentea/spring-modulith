package victor.training.modulith.catalog.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import victor.training.modulith.shared.ProductId;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, ProductId> {
  List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String namePart);
}
