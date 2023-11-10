package victor.training.modulith.shipping.impl;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "shipping")
public interface ShippingProviderApi {
  @PostMapping
  String requestShipment(@RequestParam String pickupAddress, @RequestParam String deliveryAddress);
}
