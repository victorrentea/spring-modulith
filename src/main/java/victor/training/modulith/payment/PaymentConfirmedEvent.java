package victor.training.modulith.payment;

// PROS(events): you have to NAME the event clearly
// PROS(events): you can add more listeners without chaning the code (OCP)
// PROS(events): you state that you just NOTIFY not CALL (i don't need a result)
// CONS(events): they make the code much harder to navigate
// CONS(events): when multiple listener: who runs first: IT SHOULD NOT MATTER!!
public record PaymentConfirmedEvent(long orderId, boolean ok) {
}

// Prereq: I could introduce an event because PAYMENT expected nothing of ORDER when payment is confirmed (void return)
// SURPRISE:  by default Spring dispatches the event to ALL listeners
//    in the same thread using the same transaction of the publisher( if any)

// Fix: @Async on @EventListener moves the processing on another thread/transaction => errors are trickier to track
// RISK: in case of crash, the event is lost!!!

// Fix: @ApplicationModuleListener stores the event in DB and
// RISK: too young

// Fix: Send the other module a Rabbit/Kafka message