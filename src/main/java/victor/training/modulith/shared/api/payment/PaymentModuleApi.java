package victor.training.modulith.shared.api.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.payment.impl.PayPalGatewayClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentModuleApi {
  private final PayPalGatewayClient payPalGatewayClient;

  public String generatePaymentUrl(long orderId, double total) { // TODO move to 'payment' module
    // payment gateway implementation details
    log.info("Request payment url for order id: " + orderId);
    String gatewayUrl = payPalGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
    return gatewayUrl + "&orderId=" + orderId;
  }
}
