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

  private boolean inStock;
  // how to keep this in sync with inventory.Stock level ?
  // a) FORCE THEM TO CALL US WHEN THEY UPDATE = abuse; who is CATALOG for INVENTORY.
  // b) POLL THEM EVERY 1/5/20s FOR ALL THE 10 M stocks?. or "what's new?"... = inconsistency window=?
  // c) subscribe to a JMS topic StockUpdatedEvent{productId,newStock} = complexity

  @OneToMany(mappedBy = "product")
  private List<ProductReview> reviews = new ArrayList<>();
}
