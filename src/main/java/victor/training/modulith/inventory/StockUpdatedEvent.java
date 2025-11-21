package victor.training.modulith.inventory;

import victor.training.modulith.inventory.model.Stock;

public record StockUpdatedEvent(
    long productId,
    int newStock
//    ,Stock stock // leaking my domain model out = NEVER
) {
}
