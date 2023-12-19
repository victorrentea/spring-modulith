package victor.training.modulith.catalog.impl;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.time.LocalDateTime;

@Entity
@Data // sorry
public class Product {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String description;

  private Double price;

  private boolean inStock = true;// data replication

  // optimistic locking
//  @Version
////  private Long version;
//  private LocalDateTime version;

  // audit column

}
