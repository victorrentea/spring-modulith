package victor.training.modulith.shipping.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Spring Modulith identifies properties / module (see index.adoc)
@ConfigurationProperties(prefix = "shipping")
public record ShippingProperties(
    String shippingFromAddress
) {
}
