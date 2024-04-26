package victor.training.modulith.catalog.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
//  List<Product> searchByNameLikeIgnoreCase(String namePart, PageRequest pageRequest);

  // #1 migrate data
    List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String namePart, PageRequest pageRequest);

  // #2
//  @Query("""
//      SELECT p FROM Product p
//      JOIN TODO
//      WHERE UPPER(p.name) LIKE UPPER(?1)
//      AND stock.stock > 0""")
//  List<Product> searchInStockByName(String namePart, PageRequest pageRequest);
}


//"""
//      SELECT p FROM Product p
//      JOIN StockView s on p.id = s.productId
//      WHERE UPPER(p.name) LIKE UPPER(?1)
//      AND s.stock > 0"""