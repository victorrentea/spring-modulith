package victor.training.modulith.payment;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.payment.impl.PaymentGatewayClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentModule {
  private final PaymentGatewayClient paymentGatewayClient;

  public String generatePaymentUrl(long orderId, double total) {
    log.info("Request payment url for orderid: " + orderId);
    return paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
  }

}
