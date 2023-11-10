package victor.training.modulith.payment;

import org.springframework.web.bind.annotation.RequestBody;

public record PaymentResultEvent(
    long orderId,
    boolean ok
) {
}
