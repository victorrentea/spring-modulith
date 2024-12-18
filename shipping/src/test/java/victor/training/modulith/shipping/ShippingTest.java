package victor.training.modulith.shipping;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.PublishedEvents;
import victor.training.modulith.shipping.in.rest.ShippingProviderWebHookApi;
import victor.training.modulith.shared.internalapi.shipping.ShippingResultEvent;

import static org.assertj.core.api.Assertions.assertThat;

//@ApplicationModuleTest
public class ShippingTest {
  @Autowired
  ShippingProviderWebHookApi sut;
  @Test
  @Disabled
  void callback(PublishedEvents publishedEvents) {
    // alternative: mockMVC
    sut.shippedStatus(1L, true);

    assertThat(publishedEvents.ofType(ShippingResultEvent.class))
        .containsExactly(new ShippingResultEvent(1L, true));

  }
}
