package victor.training.modulith.inventory;

public record StockChangedEvent(long productId, boolean available) {
}
