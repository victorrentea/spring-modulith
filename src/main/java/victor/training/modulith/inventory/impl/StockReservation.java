package victor.training.modulith.inventory.impl;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
  private Long productId;

  @NotNull
  private Integer items;

  private LocalDateTime createdAt = LocalDateTime.now();
}
