package victor.training.modulith.shipping.out.infra;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("shipping-provider")
public interface ShippingProviderClient {
  @PostMapping("create-shipping")
//  @Timed
//  @LogToKibana
  String requestShipment(@RequestParam String pickupAddress,
                         @RequestParam String deliveryAddress,
                         @RequestParam long orderId);
}
