package victor.training.modulith.catalog.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
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
