package victor.training.modulith.payment;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.payment.impl.PaymentGatewayApi;

@Service
@RequiredArgsConstructor
public class PaymentModule {
  private final PaymentGatewayApi paymentGatewayApi;

  public String generatePaymentUrl(Long orderId, @NotNull Double total) {
    return paymentGatewayApi.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
  }

}
