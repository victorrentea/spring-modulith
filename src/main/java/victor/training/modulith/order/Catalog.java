package victor.training.modulith.order;

import victor.training.modulith.shared.ProductId;

import java.util.Collection;
import java.util.Map;

public interface Catalog {
  Map<ProductId, Double> getManyPrices(Collection<ProductId> ids);
}
