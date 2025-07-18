package victor.training.modulith.shipping.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.shipping.ShippingInternalApi;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingInternalApiImpl implements ShippingInternalApi {
  private final ShippingService shippingService;

  @Override
  public String requestShipment(long orderId, String customerAddress) {
    return shippingService.requestShipment(orderId, customerAddress);
  }
}
