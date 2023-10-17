package victor.training.modulith.inventory.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.modulith.inventory.BackInStockEvent;
import victor.training.modulith.inventory.OutOfStockEvent;

@Getter
@ToString
@Entity
public class Stock extends AbstractAggregateRoot<Stock> {
  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  @Setter
  private Long productId;

  @NotNull
  @Setter
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
