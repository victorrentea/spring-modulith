package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.order.impl.Customer;
import victor.training.modulith.order.impl.CustomerRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderModule {
  private final CustomerRepo customerRepo;

  public record CustomerDto(String id, String email, String address) {}

  public CustomerDto getCustomer(String customerId) {
    Customer customer = customerRepo.findById(customerId).orElseThrow();
    return new CustomerDto(customer.id(), customer.address(), customer.address());
  }

  @EventListener(ApplicationStartedEvent.class)
  void initialData() {
    customerRepo.save(new Customer(
        "margareta",
        "Margareta",
        "Bucharest",
        "margareta@example.com"));
  }
}
