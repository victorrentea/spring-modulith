package victor.training.modulith.order.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import victor.training.modulith.shared.Adapter;

@Adapter
@RequiredArgsConstructor
public class ShipmentService {
  private final ShipmentApi shipmentApi;

  public String requestShipment(String customerAddress) {
    return shipmentApi.requestShipment("our-warehouse", customerAddress);
  }
  
  @FeignClient(name = "shipment")
  public interface ShipmentApi {
    @PostMapping
    String requestShipment(@RequestParam String pickupAddress, @RequestParam String deliveryAddress);
  }

}
