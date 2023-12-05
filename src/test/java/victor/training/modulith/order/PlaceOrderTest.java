//package victor.training.modulith.order;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.event.EventListener;
//import org.springframework.modulith.test.ApplicationModuleTest;
//import org.springframework.modulith.test.AssertablePublishedEvents;
//import org.springframework.modulith.test.PublishedEvents;
//import org.springframework.modulith.test.Scenario;
//import victor.training.modulith.order.impl.Order;
//import victor.training.modulith.order.impl.OrderRepo;
//import victor.training.modulith.order.impl.PlaceOrderRest;
//import victor.training.modulith.order.impl.PlaceOrderRest.PlaceOrderRequest;
//import victor.training.modulith.payment.PaymentModule;
//import victor.training.modulith.payment.PaymentResultEvent;
//import victor.training.modulith.shared.LineItem;
//import victor.training.modulith.shipping.ShippingModule;
//import victor.training.modulith.shipping.ShippingResultEvent;
//
//import java.time.Duration;
//import java.util.List;
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import static victor.training.modulith.order.OrderStatus.*;
//
//@Slf4j
//@ApplicationModuleTest // TODO (mode=..)
//public class PlaceOrderTest {
//  public static final double PRICE = 10d;
//  public static final long PRODUCT_ID = 1L;
//  @MockBean
//  ShippingModule shippingModule;
//  @MockBean
//  CatalogModuleApi catalogModuleApi;
//  @MockBean
//  InventoryModuleApi inventoryModuleApi;
//  @MockBean
//  PaymentModule paymentModule;
//  @Autowired
//  ObjectMapper objectMapper;
//
//  @Autowired
//  PlaceOrderRest sut;
//  @Autowired
//  OrderRepo orderRepo;
//
//  @BeforeEach
//  @AfterEach
//  final void cleanupDatabase() {
//    orderRepo.deleteAll();
//  }
//
//  @Test
//  void placeOrder(PublishedEvents publishedEvents) {
//    when(catalogModuleApi.getManyPrices(List.of(PRODUCT_ID))).thenReturn(Map.of(PRODUCT_ID, PRICE));
//    List<LineItem> items = List.of(new LineItem(PRODUCT_ID, 2));
//    PlaceOrderRequest request = new PlaceOrderRequest("customerId", items, "address");
//    when(paymentModule.generatePaymentUrl(anyLong(), eq(20d))).thenReturn("payurl");
//
//    String result = sut.placeOrder(request);
//
//    assertThat(result).startsWith("payurl");
//    verify(inventoryModuleApi).reserveStock(anyLong(), eq(items));
//  }
//
//  @Test
//  void onShipped(Scenario scenario) {
//    Long orderId = orderRepo.save(new Order()
//        .customerId("customerId")
//        .shippingAddress("address")
//        .items(Map.of(1L, 1))
//        .total(1d)
//        .paid(true)
//        .scheduleForShipping("tr")
//    ).id();
//
//    scenario.publish(new ShippingResultEvent(orderId, true))
//        .andWaitAtMost(Duration.ofMillis(500))
//        .andWaitForEventOfType(OrderStatusChangedEvent.class)
//        .matching(e -> e.status() == SHIPPING_COMPLETED)
//        .toArrive();
//
//    assertThat(orderRepo.findById(orderId).orElseThrow().status())
//        .isEqualTo(SHIPPING_COMPLETED);
//  }
//  @Test
//  void onPaid(Scenario scenario) {
//    Long orderId = orderRepo.save(new Order()
//        .customerId("customerId")
//        .shippingAddress("address")
//        .items(Map.of(1L, 1))
//        .total(1d)
//    ).id();
//    when(shippingModule.requestShipment(anyLong(), anyString())).thenReturn("trackingNumber");
//
//    scenario.publish(new PaymentResultEvent(orderId, true))
//        .andWaitForEventOfType(OrderStatusChangedEvent.class)
//        .matching(e -> e.status() == SHIPPING_IN_PROGRESS) // filter
//        .toArrive(); // block test
//
//    Order order = orderRepo.findById(orderId).orElseThrow();
//    assertThat(order.shippingTrackingNumber()).isEqualTo("trackingNumber");
//    assertThat(order.status()).isEqualTo(SHIPPING_IN_PROGRESS);
//    verify(shippingModule).requestShipment(orderId, "address");
//  }
//
//}
