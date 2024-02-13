package victor.training.modulith.inventory;

import org.springframework.context.ApplicationEvent;

public record OutOfStockEvent(long productId) {
}
