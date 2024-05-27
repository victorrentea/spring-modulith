package victor.training.modulith.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.catalog.impl.Product;
import victor.training.modulith.catalog.impl.ProductRepo;
import victor.training.modulith.shared.api.order.CatalogModuleInterface;

import java.util.Collection;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class CatalogModuleApi implements CatalogModuleInterface {
  private final ProductRepo productRepo;

  @Override
  public Map<Long, Double> getManyPrices(Collection<Long> ids) {
    return productRepo.findAllById(ids).stream()
        .collect(toMap(Product::id, Product::price));
  }

}
