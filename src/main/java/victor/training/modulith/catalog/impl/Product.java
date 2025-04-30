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

//  private Integer stock; // replicated from inventory
  private Boolean inStock; // store less data!

  // how to keep this in sync with Stock#items
  // a) SUBSCRIBE to their events 💖, microservice friendly
  // b) POLLING: periodically??⏱️ check ALL their state
  // c) THEY CALL ME on each stock change? my crash crashes them,
  // d) replicate data over night via files.


  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();
}
