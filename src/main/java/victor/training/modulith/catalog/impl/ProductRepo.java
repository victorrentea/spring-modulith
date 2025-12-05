package victor.training.modulith.catalog.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import victor.training.modulith.catalog.impl.SearchApi.ProductSearchCriteria;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
//  @Query("""
//          SELECT product
//          FROM Product product
//          JOIN Stock stock ON stock.productId = product.id ❌ join TABLE of another schema/module
//          WHERE UPPER(product.name) LIKE UPPER('%' || :name || '%')
//          AND UPPER(product.description) LIKE UPPER('%' || :description || '%')
//          AND stock.items > 0
//      """)

//  @Query("""
//          SELECT product
//          FROM Product product
//          JOIN StockView stock ON stock.productId = product.id ✅±
//          WHERE UPPER(product.name) LIKE UPPER('%' || :name || '%')
//          AND UPPER(product.description) LIKE UPPER('%' || :description || '%')
//          AND stock.stock > 0
//      """)

  @Query("""
          SELECT product 
          FROM Product product
          WHERE UPPER(product.name) LIKE UPPER('%' || :name || '%')
          AND UPPER(product.description) LIKE UPPER('%' || :description || '%')
          AND product.inStock
      """)
  List<Product> search(String name, String description, PageRequest pageRequest);











  List<Product> searchByNameLikeIgnoreCase(String namePart, PageRequest pageRequest);

  // #1 migrate data to Product:inStock:boolean - if we plan to extract a microservice out
    List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String namePart, PageRequest pageRequest);

  // #2 join a VIEW exposed by inventory - if we keep on modulith for longer
  @Query("""
      SELECT product FROM Product product
      JOIN StockView stock ON product.id = stock.productId
      WHERE UPPER(product.name) LIKE UPPER('%' || :name || '%')
      AND UPPER(product.description) LIKE UPPER('%' || :description || '%')
      AND stock.stock > 0""")
  List<Product> searchJoinView(String name,  String description,  PageRequest pageRequest);


  @Query("""
      SELECT product FROM Product product
      JOIN Stock stock ON product.id = stock.productId
      WHERE UPPER(product.name) LIKE UPPER('%' || :name || '%')
      AND UPPER(product.description) LIKE UPPER('%' || :description || '%')
      AND stock.items > 0""")
  List<Product> searchJoinTable(String name,  String description,  PageRequest pageRequest);
}
