package victor.training.modulith.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.View;

// WARNING! THIS SHOULD be the last resort.
// ALWAYS PREFER CALLS THROUGH INTERNAL APIs.
// (exports/search) > massive access to daya
@Entity  // part of the published internal API of the Inventory Module

// a ADR justifying explaining WHY do I really need this view (with all of its columns)
// explained without chatGPT! maniually!
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