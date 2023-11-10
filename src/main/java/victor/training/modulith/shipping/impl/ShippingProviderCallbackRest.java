package victor.training.modulith.shipping.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;
import victor.training.modulith.shipping.ShippingResultEvent;

@RestController
@RequiredArgsConstructor
public class ShippingProviderCallbackRest {
  private final ApplicationEventPublisher publisher;
  @PutMapping("shipping/{orderId}/status")
  public String shippedStatus(@PathVariable long orderId, @RequestBody boolean ok) {
    publisher.publishEvent(new ShippingResultEvent(orderId, ok));
    return "Shipping callback received";
  }
}
