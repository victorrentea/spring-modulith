package victor.training.modulith.boot;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.customer.impl.Customer;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class InitialData {
  private final EntityManager entityManager;

  @EventListener(ApplicationStartedEvent.class)
  @Transactional
  public void atStartup() {
    entityManager.persist(new Product()
        .name("iPhone")
        .description("Hipster Phone")
        .inStock(true)
        .price(1000d));
    entityManager.persist(new Customer(
        "margareta",
        "Margareta",
        "Bucharest",
        "margareta@example.com"));

  }
}
