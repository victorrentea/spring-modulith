package victor.training.modulith.payment;

//record PaymentSuccessfulEvent
//record PaymentWasSuccessfulEvent
public record PaymentProcessedEvent(long orderId, boolean ok) {
}
