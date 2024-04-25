package victor.training.modulith.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class StockView {
  @Id
  @Column(insertable = false)
  private Long productId;

  private Integer stock;
}
