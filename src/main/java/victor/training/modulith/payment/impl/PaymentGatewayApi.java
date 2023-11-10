package victor.training.modulith.payment.impl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payment")
public interface PaymentGatewayApi {
  @GetMapping
  String generatePaymentLink(@RequestParam String redirectUrl, @RequestParam Double total, @RequestParam String clientApp);
}
