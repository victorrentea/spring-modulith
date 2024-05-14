package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import victor.training.modulith.order.internalapi.CatalogModuleInterface;
import victor.training.modulith.order.internalapi.InventoryModuleInterface;
import victor.training.modulith.order.internalapi.OrderStatus;
import victor.training.modulith.payment.PaymentModuleApi;
import victor.training.modulith.payment.PaymentReceivedEvent;
import victor.training.modulith.shared.LineItem;
import victor.training.modulith.shipping.in.api.ShippingModuleApi;
import victor.training.modulith.shipping.out.event.ShippingResultEvent;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;
import static victor.training.modulith.order.impl.OrderRestApi.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepo orderRepo;
  private final CatalogModuleInterface catalogModule;
  private final InventoryModuleInterface inventoryModule;
  private final PaymentModuleApi paymentService;
  private final ShippingModuleApi shippingModule;

  public Order getOrder(long orderId) {
    return orderRepo.findById(orderId).orElseThrow();
  }

  public String doPlaceOrder(PlaceOrderRequest request) {
    List<Long> productIds = request.items().stream().map(LineItem::productId).toList();
    Map<Long, Double> prices = catalogModule.getManyPrices(productIds);
    Map<Long, Integer> items = request.items().stream().collect(toMap(LineItem::productId, LineItem::count));
    double totalPrice = request.items().stream().mapToDouble(e -> e.count() * prices.get(e.productId())).sum();
    Order order = new Order()
        .items(items)
        .shippingAddress(request.shippingAddress())
        .customerId(request.customerId())
        .total(totalPrice);
    orderRepo.save(order);
    inventoryModule.reserveStock(order.id(), request.items());
    return paymentService.generatePaymentUrl(order.id(), order.total());
  }
//  @org.springframework.core.annotation.Order// dont

  @EventListener // (A) sync, in same tx
//  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) // (B) sync, in new tx after the first commits
//  @EventListener @Async //  (C) async, in new tx. keeps event in a mem queue (FRAGILE vs CRASH) until a worker thread is available;
//  @ApplicationModuleListener // (D) async+persisted in DB  by spring-modulith
//  @KafkaListener // (E) async+persisted in Kafka (spring-kafka) with an external Kafka broker
  public void onPaymentReceived(PaymentReceivedEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }

  @ApplicationModuleListener
  public void onShippingResultEvent(ShippingResultEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.shipped(event.ok());
    orderRepo.save(order); // without @Test fails, as events not published
  }
}
