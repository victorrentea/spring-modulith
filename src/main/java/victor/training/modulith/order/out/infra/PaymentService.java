package victor.training.modulith.order.out.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import victor.training.modulith.order.domain.Order;
import victor.training.modulith.shared.Adapter;

@Adapter
@RequiredArgsConstructor
public class PaymentService {
  private final PaymentGatewayApi paymentGatewayApi;

  public String generatePaymentUrl(Order order) {
    return paymentGatewayApi.generatePaymentLink("order/"+order.id()+"/payment-accepted", order.total(), "modulith-app");
  }

  @FeignClient(name = "payment")
  public interface PaymentGatewayApi {
    @GetMapping
    String generatePaymentLink(String redirectUrl, Double total, String clientApp);
  }

}
