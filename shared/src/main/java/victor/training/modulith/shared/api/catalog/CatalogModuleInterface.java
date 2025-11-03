package victor.training.modulith.shared.api.catalog;

import java.util.Collection;
import java.util.Map;

// Dependency Inversion solves a cycle
public interface CatalogModuleInterface {
  Map<Long, Double> getManyPrices(Collection<Long> ids);
}
