package victor.training.modulith.notification.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.shared.api.order.OrderStatusChangedEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

  @EventListener
  private void sendPaymentConfirmedEmail(OrderStatusChangedEvent event) {
    log.info("Sending 📧 'Order {} Confirmed' email to {}", event.orderId()/*, customerEmail*/);
  }

  // TODO call this when order status goes to 'SHIPPING_IN_PROGRESS'
  private void sendOrderShippedEmail(OrderStatusChangedEvent event, String customerEmail) {
    log.info("Sending 📧 'Order {} Shipped' email to {}", event.orderId(), customerEmail);
  }
}
