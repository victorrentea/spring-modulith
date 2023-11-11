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
  private final CustomerModule customerModule;

  @ApplicationModuleListener
  public void onOrderStatusChanged(OrderStatusChangedEvent event) {
    // TODO 5 explore events flavors
    //  1) Notification
    //  2) Event-Carried State Transfer
    String customerId = event.customerId();
    CustomerDto customer = customerModule.getCustomer(customerId);
    String customerEmail = customer.email();
    if (event.status() == OrderStatus.PAYMENT_APPROVED) {
      log.info("Sending 📧 'Order {} Confirmed' email to {}", event.orderId(), customerEmail);
    }
    if (event.status() == OrderStatus.SHIPPING_IN_PROGRESS) {
      log.info("Sending 📧 'Order {} Shipped' email to {}", event.orderId(), customerEmail);
    }
  }
}
