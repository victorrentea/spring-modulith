package victor.training.modulith.inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Entity
@Data
@Table(schema = "inventory")
@SequenceGenerator(name = "stock_reservation_seq", schema = "inventory")
public class StockReservation {
  @Id
  @GeneratedValue(generator = "stock_reservation_seq")
  private Long id;

  @NotNull
  private Long orderId;

  @NotNull
  private Long productId;

  @NotNull
  private Integer items;

  private LocalDateTime createdAt = now();
}
