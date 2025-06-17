package victor.training.modulith.shipping.in.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.shipping.app.ShippingService;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingInternalApiImpl implements victor.training.modulith.shared.api.shipping.ShippingInternalApi {
  private final ShippingService shippingService;

  @Override
  public String requestShipment(long orderId, String customerAddress) {
    return shippingService.requestShipment(orderId, customerAddress);
  }
}
