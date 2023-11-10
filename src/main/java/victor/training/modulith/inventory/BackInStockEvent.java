package victor.training.modulith.inventory;

import victor.training.modulith.shared.ProductId;

public record BackInStockEvent(ProductId productId) {
}
