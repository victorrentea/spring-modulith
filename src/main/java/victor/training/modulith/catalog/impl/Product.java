package victor.training.modulith.catalog.impl;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.SequenceGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import victor.training.modulith.common.ProductId;

@Entity
@Data
@GenericGenerator(name = "productIdGenerator", type = Product.ProductIdGenerator.class)
@SequenceGenerator(name="unused",sequenceName = "product_seq")
public class Product {
  @EmbeddedId
  @GeneratedValue(generator = "productIdGenerator")
  private ProductId id;

  private String name;

  private String description;

  private boolean inStock;

  private Double price;

  public static class ProductIdGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
      System.out.println("select next value");
      Long nextId = session.createNativeQuery("select nextval('product_seq')", Long.class).getSingleResult();
      return new ProductId(nextId);
    }
  }
}
