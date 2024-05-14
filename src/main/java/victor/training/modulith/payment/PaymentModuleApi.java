package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.internalapi.PaymentUrlGenerator;
import victor.training.modulith.payment.impl.PaymentGatewayClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentModuleApi implements PaymentUrlGenerator {
  private final PaymentGatewayClient paymentGatewayClient;

  @Override
  public String generatePaymentUrl(long orderId, double total) { // TODO move to 'payment' module
    // payment gateway implementation details
    log.info("Request payment url for order id: " + orderId);
    String gatewayUrl = paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
    return gatewayUrl + "&orderId=" + orderId;
  }
}