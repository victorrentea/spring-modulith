package victor.training.modulith.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.customer.app.Customer;
import victor.training.modulith.customer.app.CustomerRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerModule {
  private final CustomerRepo customerRepo;

  public record CustomerDto(String id, String email, String address) {}

  public CustomerDto getCustomer(String customerId) {
    Customer customer = customerRepo.findById(customerId).orElseThrow();
    return new CustomerDto(customer.id(), customer.address(), customer.address());
  }

}