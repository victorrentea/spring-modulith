package victor.training.modulith.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.View;

@Entity
//@Subselect // older version
@View(query = """
    select STOCK.PRODUCT_ID, STOCK.ITEMS as STOCK
    from STOCK
    """)
@Getter
@Immutable // is never INSERT/UPDATE/DELETE
public class StockView { // part of the PUBLIC API of Inventory Module
  @Id
  private long productId;

  // these fields contract of Inventory Modules
  private Integer stock;
}