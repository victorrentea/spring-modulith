package victor.training.modulith.catalog;

import java.util.Collection;
import java.util.Map;

public interface CatalogInternalApi {
  Map<Long, Double> getManyPrices(Collection<Long> ids);
}
