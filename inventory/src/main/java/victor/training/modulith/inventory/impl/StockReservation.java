package victor.training.modulith.inventory.impl;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

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

  private LocalDateTime createdAt = now();
}
