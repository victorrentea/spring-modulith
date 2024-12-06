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

//  private int stock; // PR reject: you keep too much state

  private boolean inStock; // replicating state
  // in more serious cases, a new table would be required
  // containing only the state it's relevant for me.
  // can get out of sync < fight hard NOT to do this!

  // If i need 12 fields from there, why are  we separated?

  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();
}
