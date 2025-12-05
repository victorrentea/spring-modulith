package victor.training.modulith.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.View;

// ⚠️ Last resort: Join this to a query of another module to avoid N+1 queries
// 🙁 You need to create this schema too + data if you join this view in your @ApplicationModuleTest
// ☺️ PRO: inventory-team might have a TestData populating the necessary tables so view works
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