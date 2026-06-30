package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.PaymentUrlGeneratorSPI;

@Slf4j
@Service
@RequiredArgsConstructor
//public class ApplyPayService implements PaymentUrlGeneratorSPI {// TODO move to 'payment' module
//public class GPayService implements PaymentUrlGeneratorSPI {// TODO move to 'payment' module
//public class StripeService implements PaymentUrlGeneratorSPI {// TODO move to 'payment' module
//public class PayPalService implements PaymentUrlGeneratorSPI {// TODO move to 'payment' module
public class PaymentService implements PaymentUrlGeneratorSPI {// TODO move to 'payment' module
  private final PaymentGatewayClient paymentGatewayClient;

  @Override
  public String generatePaymentUrl(long orderId, double total) {
    log.info("Request payment url for order id: {}", orderId);
    String gatewayUrl = paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
    return gatewayUrl + "&orderId=" + orderId;
  }
}
