package victor.training.modulith.order.out.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping
    String requestShipment(String pickupAddress, String deliveryAddress);
  }

}
