package victor.training.modulith.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.View;

// ⚠️ The very last resort!
// JOINING A VIEW IS BAD!!!!!!!
//   Use only when you really have to join this
//   to a query of another module to avoid N+1 queries
// eg in a paginated search in another module
@Entity
@View(query = """
    select STOCK.PRODUCT_ID, STOCK.ITEMS as STOCK
    from STOCK
    """)
@Getter
@Immutable // cannot be INSERT/UPDATE/DELETE
public class StockView { // part of the PUBLIC API of Inventory Module
  // these fields are the contract of Inventory Modules
  @Id
  private long productId;
  private Integer stock;
}