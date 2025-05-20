package victor.training.modulith.payment;

import org.springframework.context.ApplicationEvent;

public record PaymentCompletedEvent(
    long orderId, boolean ok) {
}
