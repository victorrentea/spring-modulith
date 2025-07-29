package victor.training.modulith.inventory;

public record StockUpdatedEvent(
    long productId,
  /*  long b,*/
    int newStock
) {
}
