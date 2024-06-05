package victor.training.modulith.catalog.impl;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data // sorry
public class Product {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String description;

  private Double price;

  private Double stars;

  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();

  // colona in tabela CATALOG.PRODUCTS
  private Boolean hasStock;
  // cand inventory vinde ultimul produs din stock arunca un
  // OutOfStockEvent la care catalogul pune campul pe false

  /// si invers pt BackInStockEvent

}
