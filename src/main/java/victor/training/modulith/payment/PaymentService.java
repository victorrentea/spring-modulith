package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentGatewayClient paymentGatewayClient;
//  static Map<Long,Runnable> paymentCallbacks = new HashMap<>();
  public String generatePaymentUrl(
      long orderId,
      double total) { // TODO move to 'payment' module
    // payment gateway implementation details
//    paymentCallbacks.put(orderId, r);
    log.info("Request payment url for order id: " + orderId);
    String gatewayUrl = paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
    return gatewayUrl + "&orderId=" + orderId;
  }
}
