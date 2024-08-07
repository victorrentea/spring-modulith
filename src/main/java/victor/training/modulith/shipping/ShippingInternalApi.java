package victor.training.modulith.shipping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.shipping.app.ShippingService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingInternalApi {
  private final ShippingService shippingService;

  public String requestShipment(long orderId, String customerAddress) {
    return shippingService.requestShipment(orderId, customerAddress);
  }
}
