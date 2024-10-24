package victor.training.modulith.payment;

public record PaymentCompletedEvent (
    long orderId,
    boolean success
){
}
