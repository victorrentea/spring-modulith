package victor.training.modulith.inventory;

public record StockUpdatedEvent(
    long productId,
    int newStock) {
}
