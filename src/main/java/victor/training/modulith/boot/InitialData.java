package victor.training.modulith.boot;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.customer.impl.Customer;
import victor.training.modulith.customer.impl.CustomerRepo;
import victor.training.modulith.inventory.impl.Stock;
import victor.training.modulith.inventory.impl.StockRepo;
import victor.training.modulith.common.ProductId;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class InitialData {
  private final ProductRepo productRepo;
  private final StockRepo stockRepo;
  private final CustomerRepo customerRepo;

  @EventListener(ApplicationStartedEvent.class)
  public void atStartup() {
    ProductId productId = productRepo.save(new Product()
        .name("iPhone")
        .description("Hipster Phone")
        .inStock(true)
        .price(1000d)).id();
    customerRepo.save(new Customer(
        "margareta",
        "Margareta",
        "Bucharest",
        "margareta@example.com"));
    stockRepo.save(new Stock()
        .productId(productId)
        .items(3));
  }
}
