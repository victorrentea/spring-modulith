package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentUrlGenerator implements victor.training.modulith.order.PaymentUrlGenerator {
  private final PaymentGatewayClient paymentGatewayClient;

  @Override
  public String generatePaymentUrl(long orderId, double total) { // TODO move to 'payment' module
    // payment gateway implementation details
    log.info("Request payment url for order id: " + orderId);
    String gatewayUrl = paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
    return gatewayUrl + "&orderId=" + orderId;
  }
}
