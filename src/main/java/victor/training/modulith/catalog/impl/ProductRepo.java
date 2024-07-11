package victor.training.modulith.catalog.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
  List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String namePart, PageRequest pageRequest);

  // #1 migrate data to Product:inStock:boolean
  //  List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String namePart, PageRequest pageRequest);

  // #2 join Stock domain model Entity from inventory JPQL
  // BAD because
  // 1) it couples the catalog to the inventory module
  // 2) you might misinterpret their data
//  @Query("""
//      SELECT p FROM Product p
//      JOIN Stock stock ON p.id = stock.productId
//      WHERE UPPER(p.name) LIKE UPPER(?1)
//      AND stock.items > 0""")
//  List<Product> searchInStockByName(String namePart, PageRequest pageRequest);

  // i use the published data contract
  @Query("""
      SELECT p FROM Product p
      JOIN StockView stock ON p.id = stock.productId
      WHERE UPPER(p.name) LIKE UPPER(?1)
      AND stock.stock > 0""")
  List<Product> searchInStockByName(String namePart, PageRequest pageRequest);
}
