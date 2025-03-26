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

  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();

//  enum StockStatus {
//    IN_STOCK,
//    OUT_OF_STOCK
//  }
//  @Enumerated(EnumType.STRING)
//  private StockStatus stockStatus;

  private boolean inStock;
}
