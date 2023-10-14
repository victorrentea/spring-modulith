package victor.training.modulith.inventory;

import victor.training.modulith.shared.ProductId;

public record OutOfStockEvent(ProductId productId) {
}
