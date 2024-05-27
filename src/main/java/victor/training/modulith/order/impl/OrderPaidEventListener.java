package victor.training.modulith.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.orderapi.OrderStatus;
import victor.training.modulith.paymentapi.OrderPaidEvent;
import victor.training.modulith.shipping.in.api.ShippingModuleApi;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderPaidEventListener {
  private final OrderRepo orderRepo;
  private final ShippingModuleApi shippingModule;

  // 1 = consistent si in sync
  @EventListener // = executa toti listenerii in threadul [si tranzactia] publisherului, intr-o ordine ne-specificata
  // + CONSISTENT - CODUL MAI GREU DE NAVIGAT = e practic un apel de metoda deghizat
  // Folosesti @EventLIstener doar ca tranztie catre eventuri async

  // 2
//  @EventListener @Async // = executa listenerul in alt thread, independent de publisher; poate sa-ti dea race

  //3
//  @Async @TransactionalEventListener(phase = AFTER_COMMIT) // async dar duap COMMIT din publisher
  // risk: server crash cat eventul tau inca nu a fost procesat sau eroare in procesarea eventului: eventul se pierde

  //4 (spring-modulith) cam beta sa-l pui in prod
//  @ApplicationModuleListener // ca mai sus, dar salvate intr-o tabela din DB
  // sau mai bine sistem de mesaje extern: @KafkaListener, @RabbitListener
  public void onOrderPaid(OrderPaidEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
    orderRepo.save(order);
  }
}
