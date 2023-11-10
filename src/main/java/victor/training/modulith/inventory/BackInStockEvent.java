package victor.training.modulith.inventory;

import victor.training.modulith.common.ProductId;

public record BackInStockEvent(ProductId productId) {
}
