package victor.training.modulith.shared.api.catalog;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CatalogInternalApi {
  Map<Long, Double> getManyPrices(Collection<Long> ids);
  List<ProductInternalDto> getProductNames(List<Long> productIds);
}
