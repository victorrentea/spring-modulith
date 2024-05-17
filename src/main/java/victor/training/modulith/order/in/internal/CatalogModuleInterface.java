package victor.training.modulith.order.in.internal;

import java.util.Collection;
import java.util.Map;

// Dependency Inversion was used here to solve a dependency cycle
public interface CatalogModuleInterface {
  Map<Long, Double> getManyPrices(Collection<Long> ids);
}
