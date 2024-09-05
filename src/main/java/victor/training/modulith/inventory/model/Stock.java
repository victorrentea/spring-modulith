package victor.training.modulith.inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
  private Long productId;

//  @ManyToOne private Product product; // TODO explore JPA links between @Entity in different module

  @NotNull
  private Integer items = 0;

  public boolean isEmpty() {
    return items() == 0;
  }

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
