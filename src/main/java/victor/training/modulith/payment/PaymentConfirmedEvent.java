package victor.training.modulith.payment;

public record PaymentConfirmedEvent(long orderId, boolean ok) {}
