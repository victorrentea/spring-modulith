package victor.training.modulith.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Spring Modulith identifies properties / module (see index.adoc)
@ConfigurationProperties(prefix = "payment")
public record PaymentProperties(
    String clientId
) {
}
