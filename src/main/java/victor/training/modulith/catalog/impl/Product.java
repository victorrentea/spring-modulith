package victor.training.modulith.catalog.impl;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

@Entity
@Data
public class Product {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String description;

  private Double price;

  // kept in sync via events thrown from Inventory
  private boolean inStock;
}
