package victor.training.modulith.order.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentGatewayClient paymentGatewayClient;

  public String generatePaymentUrl(Long orderId, @NotNull Double total) {
    log.info("Request payment url for orderid: " + orderId);
    return paymentGatewayClient.generatePaymentLink("order/" + orderId + "/payment-accepted", total, "modulith-app");
  }

}
