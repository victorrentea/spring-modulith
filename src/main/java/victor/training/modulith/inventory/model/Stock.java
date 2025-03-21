package victor.training.modulith.inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;

@Getter
@ToString
@Entity
@Table(schema = "inventory")
@SequenceGenerator(name = "stock_seq", schema = "inventory")
public class Stock {
  @Id
  @GeneratedValue(generator = "stock_seq")
  private Long id;

  @NotNull
  @Setter // + FK to PRODUCT.ID
  // is added a fk_stock_product by import.sql
  private Long productId;

//  @ManyToOne private Product product; // can't refer to other module's @Entity directly

  @NotNull
  private Integer items = 0;

  public Stock add(int itemsAdded) {
    if (itemsAdded <= 0) {
      throw new IllegalArgumentException("Negative: " + itemsAdded);
    }
    items += itemsAdded;
    return this;
  }

  public void remove(Integer itemsRemoved) {
    if (itemsRemoved <= 0) {
      throw new IllegalArgumentException("Negative: " + itemsRemoved);
    }
    if (itemsRemoved > items) {
      throw new IllegalArgumentException("Not enough stock to remove: " + itemsRemoved);
    }
    items -= itemsRemoved;
  }
}
