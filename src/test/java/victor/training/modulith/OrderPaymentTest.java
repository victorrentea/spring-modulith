package victor.training.modulith;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.catalog.CatalogModuleApi;
import victor.training.modulith.inventory.InventoryModuleApi;
import victor.training.modulith.inventory.repo.StockRepo;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.impl.*;
import victor.training.modulith.order.impl.PlaceOrderApi.PlaceOrderRequest;
import victor.training.modulith.shipping.in.api.ShippingModuleApi;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderPaymentTest {
  @Autowired
  MockMvc mockMvc;
  @Autowired
  PlaceOrderApi placeOrderApi;
  @Autowired
  StockRepo stockRepo;
  @MockBean
  CatalogModuleApi catalogModuleApi;
  @MockBean
  InventoryModuleApi inventoryModuleApi;
  @MockBean
  ShippingModuleApi shippingModuleApi;
  @MockBean
  PaymentGatewayClient paymentGatewayClient;
  @Autowired
  PaymentGatewayWebHookApi paymentGatewayWebHookApi;
  @Autowired
  OrderRepo orderRepo;

  @Test // TODO keep passing
  void placeOrderReturnsPaymentUrlFromGateway() {
    when(catalogModuleApi.getManyPrices(any())).thenReturn(Map.of());
    PlaceOrderRequest placeOrderRequest = new PlaceOrderRequest("customer-id", List.of(), "shipping-address");
    when(paymentGatewayClient.generatePaymentLink(any(), any(), any())).thenReturn("http://payment.com");

    String url = placeOrderApi.placeOrder(placeOrderRequest);

    assertThat(url).startsWith("http://payment.com");
  }

  @Test // TODO keep passing
  void gatewayCallbackUpdatesTheOrder() {
    Long orderId = orderRepo.save(new Order()).id();

    paymentGatewayWebHookApi.confirmPayment(orderId, true);

    verify(shippingModuleApi).requestShipment(eq(orderId), any());
    Order order = orderRepo.findById(orderId).orElseThrow();
    assertThat(order.status()).isEqualTo(OrderStatus.SHIPPING_IN_PROGRESS);
  }

}
