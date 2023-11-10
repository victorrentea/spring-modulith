package victor.training.modulith.shared;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public record LineItem(ProductId productId, int count) {
  public LineItem{
    requireNonNull(productId);
  }
}
