package victor.training.modulith.catalog.impl;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Subselect;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

@Entity
@Data // sorry
public class Product {
  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String description;

  private Double price;

//  @Formula("SELECT STOCK.ITEM FROM STOCK WHERE PRODUCT_ID = id")
  private boolean inStock;
}
