package victor.training.modulith.catalog.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
  List<Product> searchByNameLikeIgnoreCaseAndInStock(String namePart, PageRequest pageRequest);

  // prima idee: join cu @Entity lor cel drag si sfant
  /*MARLANIE: 1) nu e documentat ce inseamna .items, 2) daca-si remodeleaza @Entity imi strica queryul*/
  // sau 2: JOIN cu un @Entity VIEW expus de ailalti
  @Query("""
      SELECT p FROM Product p
      JOIN StockView s ON p.id = s.productId 
      WHERE UPPER(p.name) LIKE UPPER(?1)
      AND s.stock > 0""")
  List<Product> searchJqpl(String namePart, PageRequest pageRequest);


  // #1 migrate data to Product:inStock:boolean
  //  List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String namePart, PageRequest pageRequest);

  // #2 join an Entity from inventory
//  @Query("""
//      SELECT p FROM Product p
//      JOIN TODO
//      WHERE UPPER(p.name) LIKE UPPER(?1)
//      AND stock.stock > 0""")
//  List<Product> searchInStockByName(String namePart, PageRequest pageRequest);
}
