package victor.training.modulith.catalog.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
  List<Product> searchByNameLikeIgnoreCase(String namePart, PageRequest pageRequest);

  // #1 migrate data to Product:inStock:boolean
    List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String namePart, PageRequest pageRequest);

  // #2 join an Entity from inventory
//  @Query("""
//      SELECT p FROM Product p
//      JOIN StockView stock ON p.id = stock.productId
//      WHERE UPPER(p.name) LIKE UPPER(?1)
//      AND stock.stock > 0""")
//  List<Product> searchInStockByName(String namePart, PageRequest pageRequest);
}
