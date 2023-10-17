package victor.training.modulith.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.customer.impl.Customer;
import victor.training.modulith.customer.impl.CustomerRepo;

@Service
@RequiredArgsConstructor
public class CustomerModule {
  private final CustomerRepo customerRepo;

  @EventListener(ApplicationStartedEvent.class)
  void initialData() {
    customerRepo.save(new Customer(
        "margareta",
        "Margareta",
        "Bucharest",
        "margareta@example.com"));
  }

  public String getCustomerEmail(String customerId) {
    return customerRepo.findById(customerId).orElseThrow().email();
  }
}
