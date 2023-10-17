package victor.training.modulith.notification;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.customer.CustomerModule;
//import victor.training.modulith.order.NotificationForOrderService;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.OrderStatusChangedEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationModule {
  private final CustomerModule customerModule;

  @EventListener
  public void onOrderStatusChanged(OrderStatusChangedEvent event) {
    if (event.status() == OrderStatus.SHIPPING_IN_PROGRESS) {
      sendOrderShippedEmail(event.orderId(), event.customerId());
    } else if (event.status() == OrderStatus.SHIPPING_COMPLETED) {
      sendOrderCompletedEmail(event.orderId(), event.customerId());
    }
  }

  public void sendOrderShippedEmail(long orderId, String customerId) {
    String customerEmail = customerModule.getCustomerEmail(customerId);
    log.info("Sending 📧 'Order {} Shipped' email to {}", orderId, customerEmail);
  }
  public void sendOrderCompletedEmail(long orderId, @NotNull String customerId) {
    String customerEmail = customerModule.getCustomerEmail(customerId);
    log.info("Sending 📧 'Order {} Completed' email to {}", orderId, customerEmail);
  }

}
