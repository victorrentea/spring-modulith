package victor.training.modulith.order;

import java.util.Collection;
import java.util.Map;

public interface CatalogDoor {
  Map<Long, Double> getManyPrices(Collection<Long> ids);
}
