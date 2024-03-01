package victor.training.modulith.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import victor.training.modulith.payment.impl.PaymentService;

@Component
@RequiredArgsConstructor
public class PaymentModule {
  private final PaymentService paymentService;

  public String generatePaymentUrl(long orderId, double total) { // TODO move to 'payment' module
    return paymentService.generatePaymentUrl(orderId, total);
  }
}
