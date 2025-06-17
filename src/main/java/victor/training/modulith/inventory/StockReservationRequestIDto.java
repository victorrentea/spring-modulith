package victor.training.modulith.inventory;

import victor.training.modulith.shared.LineItem;

import java.util.List;

// Internal Dto
public record StockReservationRequestIDto(
    long orderId,
    List<LineItem> items) {
}
