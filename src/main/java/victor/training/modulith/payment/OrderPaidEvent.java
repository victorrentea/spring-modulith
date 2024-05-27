package victor.training.modulith.payment;

// daca platesc si altceva decat orderuri,
// voi vorbi de PaymentStatusUpdatedEvent(paymentId, ok)
// merita facut de pe acum...
public record OrderPaidEvent(long orderId, boolean ok) {
}
