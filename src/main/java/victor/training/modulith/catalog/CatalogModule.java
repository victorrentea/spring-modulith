package victor.training.modulith.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.order.CatalogDoor;

import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class CatalogModule implements CatalogDoor {
  private final ProductRepo productRepo;

  @Override
  public Map<Long, Double> getManyPrices(Collection<Long> ids) {
    return productRepo.findAllById(ids).stream()
        .collect(toMap(Product::id, Product::price));
  }

  @EventListener(ApplicationStartedEvent.class)
  void initialData() {
    productRepo.save(new Product()
        .name("iPhone")
        .description("Hipster Phone")
        .inStock(true)
        .price(1000d));
  }

}
