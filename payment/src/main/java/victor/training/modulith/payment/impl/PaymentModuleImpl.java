package victor.training.modulith.payment.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.api.payment.PaymentModule;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentModuleImpl implements PaymentModule {
  private final PayPalGatewayClient payPalGatewayClient;

  @Override
  public String generatePaymentUrl(long orderId, double total) { // TODO move to 'payment' module
    // payment gateway implementation details
    log.info("Request payment url for order id: " + orderId);
    String gatewayUrl = payPalGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
    return gatewayUrl + "&orderId=" + orderId;
  }
}
