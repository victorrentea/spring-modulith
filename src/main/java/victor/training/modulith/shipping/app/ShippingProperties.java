package victor.training.modulith.shipping.app;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Spring Modulith identifies module properties (see index.adoc)
@ConfigurationProperties(prefix = "shipping")
public record ShippingProperties(
    String shippingFromAddress
) {
}
