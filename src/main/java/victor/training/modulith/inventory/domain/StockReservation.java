package victor.training.modulith.inventory.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import victor.training.modulith.shared.ProductId;

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
  private ProductId productId;

  @NotNull
  private Integer items;

  private LocalDateTime createdAt = LocalDateTime.now();
}
