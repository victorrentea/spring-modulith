package victor.training.modulith.payment;

import org.springframework.context.ApplicationEvent;

public record PaymentConfirmedEvent(
    long orderId,
    boolean ok)
{
}
