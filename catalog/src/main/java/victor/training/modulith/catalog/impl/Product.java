package victor.training.modulith.catalog.impl;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

  private boolean inStock;// updated by @EventListener on StockUpdatedEvent fired by invetnory - homework
}
