package victor.training.modulith.boot;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModule;
import org.springframework.stereotype.Component;
import victor.training.modulith.catalog.domain.Product;

@Component
@RequiredArgsConstructor
public class InitialData {
  private final EntityManager entityManager;
  @PostConstruct
  public void atStartup() {
    entityManager.persist(Product.EXAMPLE);
  }
}
