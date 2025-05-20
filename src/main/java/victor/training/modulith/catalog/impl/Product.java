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

  private String description;

  private Double price;

  private Double stars;

  private boolean inStock; // projection of the state in inventory
  // kept in sync via events in-mem (ACID), or eventual(Kafka..).

  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();
}
