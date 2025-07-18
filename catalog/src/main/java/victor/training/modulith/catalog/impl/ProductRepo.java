package victor.training.modulith.catalog.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
  @Query("""
          SELECT product FROM Product product
          WHERE UPPER(product.name) LIKE UPPER('%' || :name || '%')
          AND UPPER(product.description) LIKE UPPER('%' || :description || '%')
          AND product.availableStock = true
      """)
//  @Query("""
//          SELECT product FROM Product product
//          JOIN StockView s ON s.productId = product.id
//          WHERE UPPER(product.name) LIKE UPPER('%' || :name || '%')
//          AND UPPER(product.description) LIKE UPPER('%' || :description || '%')
//          AND s.stock >0
//      """)
  List<Product> search(String name, String description, PageRequest pageRequest);

  List<Product> searchByNameLikeIgnoreCase(String namePart, PageRequest pageRequest);

  // #1 migrate data to Product:inStock:boolean - if we plan to extract a microservice out
  //  List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String namePart, PageRequest pageRequest);

  // #2 join a VIEW exposed by inventory - if we keep on modulith for longer
  @Query("""
      SELECT p FROM Product p
      JOIN StockView stock ON p.id = stock.productId
      WHERE UPPER(p.name) LIKE UPPER(?1)
      AND stock.stock > 0""")
  List<Product> searchJoinView(String namePart, PageRequest pageRequest);
}
