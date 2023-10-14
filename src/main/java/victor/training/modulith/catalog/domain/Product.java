package victor.training.modulith.catalog.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.Data;
import victor.training.modulith.shared.ProductId;

@Entity
@Data
public class Product {
  @EmbeddedId
  @GeneratedValue
  private ProductId id;

  private String name;

  private String description;

  private boolean inStock;

  private Double price;

  public static final Product EXAMPLE = new Product()
      .name("iPhone")
      .description("Hipster Phone")
      .inStock(true)
      .price(1000d);
}
