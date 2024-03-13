package victor.training.modulith.shipping.app;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.shipping.out.infra.ShippingProviderClient;


@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingService {
  private final ShippingProviderClient shippingProviderClient;
  private final ShippingProperties shippingProperties;

  public String requestShipment(long orderId, String customerAddress) {
    log.info("Request shipping at " + customerAddress);
    return shippingProviderClient.requestShipment(shippingProperties.shippingFromAddress(), customerAddress, orderId);
  }
}
