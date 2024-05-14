package victor.training.modulith.catalog.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
//  List<Product> searchByNameLikeIgnoreCase(String namePart, PageRequest pageRequest);

  // #1 migrate data to Product:inStock:boolean
    List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String namePart, PageRequest pageRequest);

  // #2 join an Entity from inventory
  @Query("""
      SELECT p FROM Product p
      JOIN StockView stock ON p.id = stock.productId
      WHERE UPPER(p.name) LIKE UPPER(?1)
      AND stock.stock > 0""")
  List<Product> searchInStockByName(String namePart, PageRequest pageRequest);

//  @Query(nativeQuery = true, value = """
//      SELECT p.* FROM CATALOG.PRODUCT p
//      JOIN INVENTORY.STOCK stock ON p.id = stock.PRODUCT_ID
//      WHERE UPPER(p.NAME) LIKE UPPER(?1)
//      AND stock.ITEMS > 0""")
//  List<Product> searchInStockByNameSQL(String namePart, PageRequest pageRequest);
}
