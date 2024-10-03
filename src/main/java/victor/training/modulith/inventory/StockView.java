package victor.training.modulith.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.View;

// ⚠️ The very last resort!
//   Use only when you really have to join this
//   to a query of another module to avoid N+1 queries

/**
 * Represents a view of stock data for products within the inventory module.
 * This view provides information on the product ID and the corresponding stock quantity.
 * It is designed to be immutable, meaning instances of this class cannot be modified after creation.
 *
 * The data for this view is retrieved using the specified query, which selects the PRODUCT_ID
 * and the number of ITEMS in stock from the STOCK table.
 *
 * This class is a part of the public API of the Inventory Module.
 *
 * Fields:
 * - productId: The ID of the product.
 * - stock: The quantity of items in stock for the product.
 */
@Entity
@View(query = """
    select STOCK.PRODUCT_ID, STOCK.ITEMS as STOCK
    from STOCK
    
    """)
//@View(query = """
//    select STOCK.PRODUCT_ID
//    from PRODUCTS_IN_STOCK
//    where items > 0
//    """)
@Getter
@Immutable // cannot be INSERT/UPDATE/DELETE
public class StockView { // part of the PUBLIC API of Inventory Module
  // these fields are the contract of Inventory Modules
  @Id
  private long productId;
  private Integer stock;
}