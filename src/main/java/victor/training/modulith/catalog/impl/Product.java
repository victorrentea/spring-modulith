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

  // DATA DUPLICATION from inventory

//  private int stock; // too much data
  private boolean inStock; // kept in sync with inventory state
  // a) scheduler every 2 minutes that downloads the stock of
  //    -ALL the products from inventory
  //    -the lastest updated since 2 min ago/since last poll
  // b) events: StockUpdatedEvent{q:0} or OutOf/BackInStockEvent on Service Bus/Kafka
  // c) debezium/CDC to auto-emit events from inventory DB

  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();
}
