package victor.training.modulith.inventory.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import victor.training.modulith.common.ProductId;

import java.time.LocalDateTime;

@Entity
@Data
public class StockReservation {
  @Id
  @GeneratedValue
  private Long id;

  @NotNull
  private Long orderId;

  @NotNull
  @AttributeOverride(name = "id", column = @Column(name = "product_id"))
  private ProductId productId;

  @NotNull
  private Integer items;

  private LocalDateTime createdAt = LocalDateTime.now();
}
