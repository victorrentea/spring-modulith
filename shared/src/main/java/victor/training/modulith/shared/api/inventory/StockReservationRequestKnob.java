package victor.training.modulith.shared.api.inventory;

import victor.training.modulith.shared.LineItem;

import java.util.List;

// "Knob" = Internal Dto 😆
public record StockReservationRequestKnob(
    long orderId,
    List<LineItem> items) {
}
