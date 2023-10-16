package victor.training.modulith.order;

import victor.training.modulith.common.ProductId;

import java.util.Collection;
import java.util.Map;

public interface CatalogDoor {
  Map<ProductId, Double> getManyPrices(Collection<ProductId> ids);
}
