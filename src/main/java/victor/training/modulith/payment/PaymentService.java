package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {// TODO move to 'payment' module
  private final PaymentGatewayClient paymentGatewayClient;

  public String generatePaymentUrl(long orderId, double total) {
    log.info("Request payment url for order id: {}", orderId);
    String gatewayUrl = paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
    return gatewayUrl + "&orderId=" + orderId;
  }
}
