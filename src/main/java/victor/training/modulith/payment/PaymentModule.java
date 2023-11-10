package victor.training.modulith.payment;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.payment.impl.PaymentGatewayClient;

@Service
@RequiredArgsConstructor
public class PaymentModule {
  private final PaymentGatewayClient paymentGatewayClient;

  public String generatePaymentUrl(Long orderId, @NotNull Double total) {
    return paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
  }

}
