package victor.training.modulith.shipping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.shipping.impl.ShippingProviderClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShippingModule {
  private final ShippingProviderClient shippingProviderClient;

  public String requestShipment(String customerAddress) {
    log.info("Request shipping at " + customerAddress);
    return shippingProviderClient.requestShipment("our-warehouse", customerAddress);
  }
  

}
