package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import victor.training.modulith.payment.impl.PaymentGatewayClient;

@RequiredArgsConstructor
@Component
@Slf4j

public class PaymentModule {
  private final PaymentGatewayClient paymentGatewayClient;
  private final PaymentProperties paymentProperties;
  public String generatePaymentUrl(long orderId, double total) { // TODO move to 'payment' module
    log.info("Request payment url for order id: " + orderId);
    String gatewayUrl = paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, paymentProperties.clientId());
    return gatewayUrl + "&orderId=" + orderId;
  }
}
