package victor.training.modulith.catalog.impl;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Data // sorry
@Table(schema = "catalog")
@SequenceGenerator(name = "product_seq", schema = "catalog")
public class Product {
  @Id
  @GeneratedValue(generator = "product_seq")
  private Long id;

  private String name;

  private String description;

  private Double price;

  private Double stars;

//  private int stock; // copy the data from inventory (master data)
  private boolean inStock; // less data

  // ** convenient for listener:
  // - out of stock, back in stock 💖 for me
  // - product availability updated (more domain) 💖 for me, in stock updated

  // ** integration events:
  // kept up to date via a K topic named:
  // - stock update {productId, total, timestamp}
  // - product stock updated  --"--
  // - product stock log = "CUD Event" from on CDC Inventory DB -> framework - json

  // ** Internal Event
  // from the point of view of Inventory business:
  //    = Internal Event in Event Sourcing
  // OutOfStock
  // StockRefilled{delta}
  // ItemReserved{count}
  // ItemReleased{count}
  // ItemSold{count}
  // ItemStolen{count}
  // RecountEvent{total}




  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();
}
