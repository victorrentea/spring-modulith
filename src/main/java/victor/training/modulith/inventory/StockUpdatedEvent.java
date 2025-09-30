package victor.training.modulith.inventory;

public record StockUpdatedEvent(
    long productId,
//    int stockChange,// "Delta Event",eg +2 => added 2 to master stock, -5 = sold 5

    /** new value of the stock, after the update*/
    int newStock // "Snapshot Event"
) {
}
