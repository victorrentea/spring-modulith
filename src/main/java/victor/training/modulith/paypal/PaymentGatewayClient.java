package victor.training.modulith.paypal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("payment-gateway")
public interface PaymentGatewayClient { // TODO move to 'payment' module
  @GetMapping("get-payment-link")
  String generatePaymentLink(@RequestParam String redirectUrl,
                             @RequestParam Double total,
                             @RequestParam String clientApp);
}
