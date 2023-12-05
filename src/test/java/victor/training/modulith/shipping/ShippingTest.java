package victor.training.modulith.shipping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;
import victor.training.modulith.shipping.impl.ShippingProviderCallbackApi;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest
public class ShippingTest {
  @Autowired
  ShippingProviderCallbackApi sut;
  @Test
  void callback(PublishedEvents publishedEvents) {
    // alternative: mockMVC
    sut.shippedStatus(1L, true);

    assertThat(publishedEvents.ofType(ShippingResultEvent.class))
        .containsExactly(new ShippingResultEvent(1L, true));

  }
}
