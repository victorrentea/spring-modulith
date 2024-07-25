package victor.training.modulith.catalog.impl;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data // sorry
@Table(schema = "catalog")
@SequenceGenerator(name = "product_seq", schema = "catalog")
public class Product {
  @Id
  @GeneratedValue(generator = "product_seq")
  private Long id;

  private String name;

  private boolean inStock;// replicaation of data. FOR MICROSERVICES
  //how to update this?
  // a) periodically cron every 5 min
  // b) event based: when stock changes, publish an event; less delay
  // both, especially a) allow a window of inconsistency
  // eg; events: OutOfStockEvent, BackInStockEvent

  // c) NEVER. AI💩: syncronously: when stock changes, update all services that need it

  private String description;

  private Double price;

  private Double stars;

  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();
}
