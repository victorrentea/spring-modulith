package victor.training.modulith.payment;

import jakarta.validation.constraints.NotNull;

public record PaymentFinishedEvent(
    long orderId,
    boolean ok) {
}
// validation on records have issues in Java EE because
// the fields are initialized