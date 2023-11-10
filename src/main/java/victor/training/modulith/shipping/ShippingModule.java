package victor.training.modulith.shipping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.shipping.impl.ShippingProviderClient;

@Service
@RequiredArgsConstructor
public class ShippingModule {
  private final ShippingProviderClient shippingProviderClient;

  public String requestShipment(String customerAddress) {
    return shippingProviderClient.requestShipment("our-warehouse", customerAddress);
  }
  

}
