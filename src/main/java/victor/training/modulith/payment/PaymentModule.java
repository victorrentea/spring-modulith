package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.payment.out.infra.PaymentGatewayClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentModule {
  private final PaymentGatewayClient paymentGatewayClient;
  public String generatePaymentUrl(long orderId, double total) {
    return paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
  }
}
