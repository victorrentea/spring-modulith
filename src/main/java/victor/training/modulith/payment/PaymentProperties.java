package victor.training.modulith.payment;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Spring Modulith identifies properties / module (see index.adoc)
@ConfigurationProperties(prefix = "payment")
public record PaymentProperties(
    String clientId
) {
}
