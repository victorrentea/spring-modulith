package victor.training.modulith.inventory;

public record StockUpdatedEvent(
    long productId,
    int newStock // snapshot, self healingm, allowing indempotent consumers
//    int deltaStock // can't reprocess it.
) {
}
