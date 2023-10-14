package victor.training.modulith.inventory.domain;

import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.modulith.shared.ProductId;
import victor.training.modulith.inventory.out.door.event.BackInStockEvent;
import victor.training.modulith.inventory.out.door.event.OutOfStockEvent;

@Data
public class Stock extends AbstractAggregateRoot<Stock> {
  @Id
  @GeneratedValue
  private Long id;

  @Embedded
  private ProductId productId;

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
