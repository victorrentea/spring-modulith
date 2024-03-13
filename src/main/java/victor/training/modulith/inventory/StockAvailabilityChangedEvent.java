package victor.training.modulith.inventory;

public record StockAvailabilityChangedEvent(
    long productId,
    boolean inStock) {
}
