package victor.training.modulith.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.catalog.CatalogInternalApi;
import victor.training.modulith.inventory.InventoryInternalApi;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.impl.*;
import victor.training.modulith.order.impl.PlaceOrderApi.PlaceOrderRequest;
import victor.training.modulith.shipping.ShippingInternalApi;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class OrderTest {
  @MockBean
  CatalogInternalApi catalogModule;
  @MockBean
  InventoryInternalApi inventoryModule;
  @MockBean
  ShippingInternalApi shippingModule;
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
    when(catalogModule.getManyPrices(any())).thenReturn(Map.of());
    PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest("customer-id", List.of(), "shipping-address");
    when(paymentGatewayClient.generatePaymentLink(any(), any(), any())).thenReturn("http://payment.com");

    String url = placeOrderApi.call(placeOrderRequest);

    verify(inventoryModule).reserveStock(any());
    assertThat(url).startsWith("http://payment.com");
  }

  @Test
  void paymentGatewayCallbackUpdatesTheOrder() {
    Long orderId = orderRepo.save(new Order()).id();

    paymentGatewayWebHookApi.confirmPayment(orderId, true);

    verify(shippingModule).requestShipment(eq(orderId), any());
    Order order = orderRepo.findById(orderId).orElseThrow();
    assertThat(order.status()).isEqualTo(OrderStatus.SHIPPING_IN_PROGRESS);
  }
}
