package victor.training.modulith.inventory.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.modulith.shared.ProductId;
import victor.training.modulith.inventory.BackInStockEvent;
import victor.training.modulith.inventory.OutOfStockEvent;

@Data
@Entity
public class Stock extends AbstractAggregateRoot<Stock> {
  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  @AttributeOverride(name = "id", column = @Column(name = "product_id"))
  private ProductId productId;

  @NotNull
  private Integer items = 0;

  public void add(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("Negative: " + n);
    }
    if (items == 0) {
      registerEvent(new BackInStockEvent(productId));
    }
    items += n;
  }

  public void remove(Integer n) {
    if (n <= 0) {
      throw new IllegalArgumentException("Negative: " + n);
    }
    if (n > items) {
      throw new IllegalArgumentException("Not enough stock to remove: " + n);
    }
    items -= n;
    if (items == 0) {
      registerEvent(new OutOfStockEvent(productId));
    }
  }
}
