package victor.training.modulith.order.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import victor.training.modulith.order.impl.Order;
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
    String generatePaymentLink(@RequestParam String redirectUrl, @RequestParam Double total, @RequestParam String clientApp);
  }

}
