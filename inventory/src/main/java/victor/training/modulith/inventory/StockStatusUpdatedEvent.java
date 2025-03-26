package victor.training.modulith.inventory;

public record StockStatusUpdatedEvent(
    long productId, boolean inStock) {
}
