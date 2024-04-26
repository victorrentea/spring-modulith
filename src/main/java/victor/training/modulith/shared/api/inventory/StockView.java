package victor.training.modulith.shared.api.inventory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
public class StockView {
  @Id
  @Column(insertable = false)
  private Long productId;

  @Column(updatable = false)
  private Integer stock;
}
