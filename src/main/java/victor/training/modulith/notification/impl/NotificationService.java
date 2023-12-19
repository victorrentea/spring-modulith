package victor.training.modulith.notification.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.customer.CustomerModule;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.OrderStatusChangedEvent;

import static victor.training.modulith.customer.CustomerModule.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

  // TODO call this when order status goes to 'PAYMENT_APPROVED'
  private void sendPaymentConfirmedEmail(OrderStatusChangedEvent event, String customerEmail) {
    log.info("Sending 📧 'Order {} Confirmed' email to {}", event.orderId(), customerEmail);
  }

  // TODO call this when order status goes to 'SHIPPING_IN_PROGRESS'
  private void sendOrderShippedEmail(OrderStatusChangedEvent event, String customerEmail) {
    log.info("Sending 📧 'Order {} Shipped' email to {}", event.orderId(), customerEmail);
  }
}
