package victor.training.modulith.catalog.impl;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import victor.training.modulith.shared.ProductId;

public class ProductIdGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
      System.out.println("select next value");
      Long nextId = session.createNativeQuery("select nextval('product_seq')", Long.class).getSingleResult();
      return new ProductId(nextId);
    }
  }