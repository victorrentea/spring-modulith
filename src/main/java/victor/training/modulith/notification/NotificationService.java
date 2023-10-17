package victor.training.modulith.notification;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.customer.CustomerModule;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
  private final CustomerModule customerModule;

  public void sendOrderShippedEmail(long orderId, String customerId) {
    String customerEmail = customerModule.getCustomerEmail(customerId);
    log.info("Sending 📧 'Order {} Shipped' email to {}", orderId, customerEmail);
  }
  public void sendOrderCompletedEmail(long orderId, @NotNull String customerId) {
    String customerEmail = customerModule.getCustomerEmail(customerId);
    log.info("Sending 📧 'Order {} Completed' email to {}", orderId, customerEmail);
  }

}
