package victor.training.modulith.catalog.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
  @Query("""
      SELECT p FROM Product p
      JOIN Stock s ON s.productId = p.id
      WHERE UPPER(p.name) LIKE UPPER(?1)
      AND s.items>0
      """) // I broke in. cheated them all. Hope Alexandre doesn't review this!
  // #1: won't work to split the DB in 2 ~> microservice @nicolas
  // #2: (a)coupling to internal Domain Model of Inventory module:
  //    (b) mis-interpret (reservations)
  List<Product> searchJoiningTheirEntity(String namePart, PageRequest pageRequest);

  // STANDARD WARNING AGAINST VIEWS: use them as a last resort.
  // >>> only to fix a demonstrated perf problem. usually for paginated search
  // call InventoryAPI until then.
  @Query("""
      SELECT p FROM Product p
      JOIN StockView s ON s.productId = p.id
      WHERE UPPER(p.name) LIKE UPPER(?1)
      AND s.stock>0
      """) // #respect them
  //    Fix: join a VIEW entity exposing strictly what THEY want, maintainted by them
  List<Product> searchJoiningTheirView(String namePart, PageRequest pageRequest);

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

  List<Product> searchByNameLikeIgnoreCaseAndInStockTrue(String name, PageRequest pageRequest);
}
