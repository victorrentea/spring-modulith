package victor.training.modulith.order.in.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.impl.Order;
import victor.training.modulith.order.impl.OrderRepo;
import victor.training.modulith.payment.PaymentResultEvent;
import victor.training.modulith.shipping.ShippingModule;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentResultListener {
//  @Value("${prop}")
//  private final int prop;
  private final OrderRepo orderRepo;
  private final ShippingModule shippingModule;

  // @KafkaListener(topics = "paymentResult") // cand scot microsrviciul
  @ApplicationModuleListener
  public void onPaymentProcessed(PaymentResultEvent event) {
    Order order = orderRepo.findById(event.orderId()).orElseThrow();
    order.paid(event.ok());
    if (order.status() == OrderStatus.PAYMENT_APPROVED) {
      String trackingNumber = shippingModule.requestShipment(order.id(), order.shippingAddress());
      order.scheduleForShipping(trackingNumber);
    }
  }
}
