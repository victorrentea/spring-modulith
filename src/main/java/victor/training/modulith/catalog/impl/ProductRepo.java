package victor.training.modulith.catalog.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
//  List<Product> searchByNameLikeIgnoreCase(String namePart);
  List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String namePart);
  // the sql that gets generated is:
  // select * from product where name like ? and in_stock = true
}
