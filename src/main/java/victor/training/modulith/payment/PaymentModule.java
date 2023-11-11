package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import victor.training.modulith.payment.config.PaymentProperties;
import victor.training.modulith.payment.out.infra.PaymentGatewayClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentModule {
  private final PaymentGatewayClient paymentGatewayClient;
  private final PaymentProperties properties;

  public String generatePaymentUrl(long orderId, double total) {
    log.info("Request payment url for order id: " + orderId);
    String gatewayUrl = paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, properties.clientId());
    return gatewayUrl + "&orderId=" + orderId;
  }
}
