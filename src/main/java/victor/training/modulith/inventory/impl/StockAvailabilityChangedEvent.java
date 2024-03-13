package victor.training.modulith.inventory.impl;

public record StockAvailabilityChangedEvent(
    long productId,
    boolean inStock) {
}
