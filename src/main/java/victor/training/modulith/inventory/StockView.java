package victor.training.modulith.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.View;

// ⚠️ Last resort: Join this to a query of another module to avoid N+1 queries
// PREFER to call InventoryInternalApi methods💖
@Entity
@View(query = """
    select STOCK.PRODUCT_ID, STOCK.ITEMS as STOCK
    from INVENTORY.STOCK
    """)
@Getter
@Immutable // can never cause an INSERT/UPDATE/DELETE
public class StockView { // part of the PUBLIC API of Inventory Module
  // these fields = the contract of Inventory module
  @Id
  private long productId;
  private Integer stock;
}