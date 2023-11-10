package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialData {
  private final ProductRepo productRepo;

  @EventListener(ApplicationStartedEvent.class)
  void initialData() {
    productRepo.save(new Product()
        .name("iPhone")
        .description("Hipster Phone")
//        .inStock(true)
        .price(1000d));
  }

}
