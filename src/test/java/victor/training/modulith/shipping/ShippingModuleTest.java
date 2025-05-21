package victor.training.modulith.shipping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;
import victor.training.modulith.shipping.in.rest.ShippingProviderWebHookApi;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest
public class ShippingModuleTest {
  @Autowired
  ShippingProviderWebHookApi sut;
  @Test
  void callback(PublishedEvents publishedEvents) {
    sut.call(1L, true);

    assertThat(publishedEvents.ofType(ShippingResultEvent.class))
        .containsExactly(new ShippingResultEvent(1L, true));
  }
}
