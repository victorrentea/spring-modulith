package victor.training.modulith.shared.api.inventory;

public record StockUpdatedEvent(
    long productId,
  /*  long b,*/
    int newStock
) {
}
