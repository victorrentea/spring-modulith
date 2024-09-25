package victor.training.modulith.payment;

public record PaymentConfirmedEvent(
    Long orderId,
    boolean ok
) {
}
// ⬆️PaymentEvent{type:CONFIRMED|REJECTED|NOT_FUNDS|PENDING} is too broad

// ⬇️should I create a second event for
// PaymentFailedEvent instead of using a boolean?
// + extra fields for failed only (reason)
// + can subscribe to only failed events
// + less volume (failed are far less in number)
// - more topics/queues, governance++
// ± -more event schemas, but event structures are more stable (AVRO FTW)

