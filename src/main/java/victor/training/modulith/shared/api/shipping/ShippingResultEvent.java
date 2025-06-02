package victor.training.modulith.shared.api.shipping;

import org.jmolecules.event.annotation.Externalized;

@Externalized// ~ Solace
public record ShippingResultEvent(long orderId, boolean ok) {
}
