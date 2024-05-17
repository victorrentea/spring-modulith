package victor.training.modulith.inventory.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
