package victor.training.modulith.customer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.customer.domain.Customer;
import victor.training.modulith.customer.domain.CustomerRepo;
import victor.training.modulith.order.in.door.OrdersDoor;
import victor.training.modulith.order.out.door.OrderConfirmedEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerModule {
  private final CustomerRepo customerRepo;
  private final OrdersDoor ordersDoor;

  @PostConstruct
  void insertInitialData() {
    customerRepo.save(new Customer("margareta", "Margareta", "Bucharest", "margareta@example.com"));
  }

  public Customer getCustomerAddress(String customerId) {
    return customerRepo.findById(customerId).orElseThrow();
  }

  @ApplicationModuleListener
  void handleOrderConfirmed(OrderConfirmedEvent e) {
    String customerId = ordersDoor.getCustomerOfOrder(e.orderId());
    Customer customer = customerRepo.findById(customerId).orElseThrow();
    log.info("Sending 'Order Confirmed' email to " + customer);
  }
}
