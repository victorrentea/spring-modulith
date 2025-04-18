package victor.training.modulith.shipping.in.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.shipping.ShippingResultEvent;

@RestController
@RequiredArgsConstructor
public class ShippingProviderWebHookApi {
  private final ApplicationEventPublisher eventPublisher;

  @PutMapping("shipping/{orderId}/status")
  @Transactional // required for @ApplicationModuleListener to fire
  public String call(@PathVariable long orderId, @RequestBody boolean ok) {
    eventPublisher.publishEvent(new ShippingResultEvent(orderId, ok));
    return "Shipping callback received";
  }
}
