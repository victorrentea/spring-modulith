package victor.training.modulith.inventory.out.door.event;

import victor.training.modulith.shared.ProductId;

public record BackInStockEvent(ProductId productId) {
}
