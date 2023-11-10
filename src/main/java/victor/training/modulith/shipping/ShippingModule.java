package victor.training.modulith.shipping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.shipping.impl.ShippingProviderApi;

@Service
@RequiredArgsConstructor
public class ShippingModule {
  private final ShippingProviderApi shippingProviderApi;

  public String requestShipment(String customerAddress) {
    return shippingProviderApi.requestShipment("our-warehouse", customerAddress);
  }
  

}
