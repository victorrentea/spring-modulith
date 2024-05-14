package victor.training.modulith.inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Entity
public class Stock {
  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  @Setter // + FK to PRODUCT.ID
  private Long productId;

//  @ManyToOne
//  private Product product;

//  @ManyToOne private Product product; // TODO explore JPA links between @Entity in different module

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
