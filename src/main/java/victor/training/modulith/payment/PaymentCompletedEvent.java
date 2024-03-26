package victor.training.modulith.payment;

// The big benefit of events is that you have to NAME them
// explicitate WHAT is interesting for others + data
// problem in a codebase: makes the code harder to navigate
public record PaymentCompletedEvent(
    long orderId,
    boolean ok) {
}
