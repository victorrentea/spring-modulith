package victor.training.modulith.shipping;

import org.jmolecules.event.annotation.Externalized;

@Externalized// ~ Solace
public record ShippingResultEvent(long orderId, boolean ok) {
}
