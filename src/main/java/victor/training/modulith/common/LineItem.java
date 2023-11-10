package victor.training.modulith.common;

import static java.util.Objects.requireNonNull;

public record LineItem(ProductId productId, int count) {
  public LineItem{
    requireNonNull(productId);
  }
}
