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

//  private Integer stock; // for tomorrow's needs

  // this replicates less data
  // builds a "read projection" useful for MY NEEDS.
  // don't store all 7 fields from events but only WHAT YOU NEED
  private Boolean inStock;

  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();
}
