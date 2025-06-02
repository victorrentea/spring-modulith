package victor.training.modulith.e2e;

import jakarta.servlet.ServletConfig;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import victor.training.modulith.shared.api.order.OrderStatus;
import victor.training.modulith.order.impl.*;
import victor.training.modulith.order.impl.PlaceOrderApi.PlaceOrderRequest;
import victor.training.modulith.payment.impl.PaymentGatewayClient;
import victor.training.modulith.payment.impl.PaymentGatewayWebHookApi;
import victor.training.modulith.shared.api.catalog.CatalogInternalApi;
import victor.training.modulith.shared.api.inventory.InventoryInternalApi;
import victor.training.modulith.shared.api.shipping.ShippingInternalApi;
import victor.training.modulith.shipping.in.rest.ShippingProviderWebHookApi;
import victor.training.modulith.shipping.out.infra.ShippingProviderClient;

import java.util.List;
import java.util.Map;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
//@Transactional
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
  @Autowired
  private ShippingProviderClient shippingProviderClient;
  @Autowired
  private ServletConfig servletConfig;

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

  @Autowired
  ShippingProviderWebHookApi shippingProviderWebHookApi;
  @Test
  void shippingCompletedUpdatesTheOrder() {
    Long orderId = orderRepo.save(new Order()
            .customerId("customer-id")
            .items(Map.of(1L, 1))
            .total(1d)
        .pay(true)
        .wasScheduleForShipping("tracking-number")).id();

    shippingProviderWebHookApi.call(orderId, true);

    Awaitility.await()
        .pollInterval(ofMillis(50))
        .timeout(ofSeconds(2))
        .untilAsserted(() -> assertThat(orderRepo.findById(orderId).orElseThrow().status())
            .isEqualTo(OrderStatus.SHIPPING_COMPLETED));
  }
}
