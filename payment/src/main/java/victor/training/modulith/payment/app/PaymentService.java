package victor.training.modulith.payment.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.PaymentProvider;
import victor.training.modulith.payment.out.PaymentGatewayClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService implements PaymentProvider {// TODO move to 'payment' module
  private final PaymentGatewayClient paymentGatewayClient;

  @Override
  public String generatePaymentUrl(long orderId, double total) {
    log.info("Request payment url for order id: {}", orderId);
    String gatewayUrl = paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
    return gatewayUrl + "&orderId=" + orderId;
  }
}
