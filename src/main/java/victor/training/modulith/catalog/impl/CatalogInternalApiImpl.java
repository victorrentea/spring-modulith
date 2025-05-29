package victor.training.modulith.catalog.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.api.catalog.ProductInternalDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class CatalogInternalApiImpl implements victor.training.modulith.shared.api.catalog.CatalogInternalApi {
  private final ProductRepo productRepo;

  @Override
  public Map<Long, Double> getManyPrices(Collection<Long> ids) {
    return productRepo.findAllById(ids).stream()
        .collect(toMap(Product::id, Product::price));
  }

  @Override
  public List<ProductInternalDto> getProductNames(List<Long> productIds) {
    return productRepo.findAllById(productIds).stream()
        .map(p -> new ProductInternalDto(p.id(), p.name()))
        .toList();
  }
}
