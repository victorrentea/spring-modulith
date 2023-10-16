package victor.training.modulith.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.inventory.BackInStockEvent;
import victor.training.modulith.inventory.OutOfStockEvent;
import victor.training.modulith.order.CatalogDoor;
import victor.training.modulith.shared.ProductId;

import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

@Service
@RequiredArgsConstructor
public class CatalogModule implements CatalogDoor {
  private final ProductRepo productRepo;

  @Override
  public Map<ProductId, Double> getManyPrices(Collection<ProductId> ids) {
    return productRepo.findAllById(ids).stream()
        .collect(toImmutableMap(Product::id, Product::price));
  }

}
