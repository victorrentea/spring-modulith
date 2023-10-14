package victor.training.modulith.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.inventory.BackInStockEvent;
import victor.training.modulith.inventory.OutOfStockEvent;
import victor.training.modulith.order.Catalog;
import victor.training.modulith.shared.ProductId;

import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.ImmutableMap.toImmutableMap;

@Service
@RequiredArgsConstructor
public class CatalogDoor implements Catalog {
  private final ProductRepo productRepo;

  @Override
  public Map<ProductId, Double> getManyPrices(Collection<ProductId> ids) {
    return productRepo.findAllById(ids).stream()
        .collect(toImmutableMap(Product::id, Product::price));
  }

  @ApplicationModuleListener
  void onOutOfStock(OutOfStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(false);
    productRepo.save(product); // not really needed due to @Transactional on @ApplicationModuleListener
  }

  @ApplicationModuleListener
  void onBackInStock(BackInStockEvent event) {
    Product product = productRepo.findById(event.productId()).orElseThrow();
    product.inStock(true);
    productRepo.save(product);
  }
}
