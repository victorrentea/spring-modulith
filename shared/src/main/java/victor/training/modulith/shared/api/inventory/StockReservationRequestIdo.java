package victor.training.modulith.shared.api.inventory;

import victor.training.modulith.shared.LineItem;

import java.util.List;

// "Knob" = Internal Dto 😆
public record StockReservationRequestIdo(
    long orderId,
    List<LineItem> items) {
}
