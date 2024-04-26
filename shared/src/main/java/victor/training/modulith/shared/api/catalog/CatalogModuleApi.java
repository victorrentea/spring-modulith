package victor.training.modulith.shared.api.catalog;

import java.util.Collection;
import java.util.Map;

// Dependency Inversion was used here to solve a dependency cycle
public interface CatalogModuleApi {
  Map<Long, Double> getManyPrices(Collection<Long> ids);
}
