package victor.training.modulith.inventory;

public record InventoryItemUpdatedEvent(
    long productId,
    int stock,
    String warehouseId
) {
}
