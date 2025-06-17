package victor.training.modulith.inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.AbstractAggregateRoot;
import victor.training.modulith.catalog.impl.Product;

@Getter
@ToString
@Entity
@Table(schema = "inventory")
@SequenceGenerator(name = "stock_seq", schema = "inventory")
public class Stock {
  @Id
  @GeneratedValue(generator = "stock_seq")
  private Long id;

// ❌ don't reference other module's @Entity directly
//  @ManyToOne
//  private Product product; // BAD naive OOP link

// ✅ instead, only store ID of the catalog.Product
  @NotNull
  @Setter // + fk_stock_product to PRODUCT (import.sql)
  private Long productId; // GOOD + explicit productRepo.fbi(id)

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
      throw new IllegalArgumentException("Must substract a positive number: " + itemsRemoved);
    }
    if (itemsRemoved > items) {
      throw new IllegalArgumentException("Not enough stock to remove: " + itemsRemoved);
    }
    items -= itemsRemoved;
  }
}
