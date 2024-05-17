package victor.training.modulith.inventory;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.View;
// TODO explore
//@Subselect("""
//    select STOCK.PRODUCT_ID, STOCK.ITEMS as STOCK
//    from STOCK
//    """)

@Entity
@View(query = """
    select STOCK.PRODUCT_ID, STOCK.ITEMS as STOCK
    from STOCK
    """)
@Getter
@Immutable // can't change data
public class StockView { // PUBLIC API of Inventory Module
  //names of these fields never change = contract of Inventory modul // TODO take this
  @Id
  private long productId;

  private Integer stock;
}
