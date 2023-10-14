package victor.training.modulith.catalog.impl;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import victor.training.modulith.shared.ProductId;

@Entity
@Data
@GenericGenerator(name = "productIdGenerator", type = ProductIdGenerator.class)
@SequenceGenerator(name="unused",sequenceName = "product_seq")
public class Product {
  @EmbeddedId
  @GeneratedValue(generator = "productIdGenerator")
  private ProductId id;

  private String name;

  private String description;

  private boolean inStock;

  private Double price;
}
