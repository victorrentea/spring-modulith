package victor.training.modulith.notification;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.customer.CustomerModule;
import victor.training.modulith.order.NotificationForOrderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationModule implements NotificationForOrderService {
  private final CustomerModule customerModule;

  @Override
  public void sendOrderShippedEmail(long orderId, String customerId) {
    String customerEmail = customerModule.getCustomerEmail(customerId);
    log.info("Sending 📧 'Order {} Shipped' email to {}", orderId, customerEmail);
  }
  @Override
  public void sendOrderCompletedEmail(long orderId, @NotNull String customerId) {
    String customerEmail = customerModule.getCustomerEmail(customerId);
    log.info("Sending 📧 'Order {} Completed' email to {}", orderId, customerEmail);
  }

}
