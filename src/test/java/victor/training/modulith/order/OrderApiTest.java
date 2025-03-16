package victor.training.modulith.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.catalog.CatalogInternalApi;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.impl.*;
import victor.training.modulith.order.impl.PlaceOrderApi.PlaceOrderRequest;
import victor.training.modulith.shipping.ShippingInternalApi;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ApplicationModuleTest
@Transactional
public class OrderApiTest {
  @MockBean
  CatalogInternalApi catalogApi;
  @MockBean
  InventoryInternalApi inventoryModuleApi;
  @MockBean
  ShippingInternalApi shippingInternalApi;
  @MockBean
  PaymentGatewayClient paymentGatewayClient;

  @Autowired
  PlaceOrderApi placeOrderApi;
  @Autowired
  PaymentGatewayWebHookApi paymentGatewayWebHookApi;
  @Autowired
  OrderRepo orderRepo;

  @Test
  void placeOrderReturnsPaymentUrlFromGateway() {
    when(catalogApi.getManyPrices(any())).thenReturn(Map.of());
    PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest("customer-id", List.of(), "shipping-address");
    when(paymentGatewayClient.generatePaymentLink(any(), any(), any())).thenReturn("http://payment.com");

    String url = placeOrderApi.call(placeOrderRequest);

    verify(inventoryModuleApi).reserveStock(anyLong(), any());
    assertThat(url).startsWith("http://payment.com");
  }

  @Test
  void gatewayCallbackUpdatesTheOrder() {
    Long orderId = orderRepo.save(new Order()).id();

    paymentGatewayWebHookApi.confirmPayment(orderId, true);

    verify(shippingInternalApi).requestShipment(eq(orderId), any());
    Order order = orderRepo.findById(orderId).orElseThrow();
    assertThat(order.status()).isEqualTo(OrderStatus.SHIPPING_IN_PROGRESS);
  }

}
