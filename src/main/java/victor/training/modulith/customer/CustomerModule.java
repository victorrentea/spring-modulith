package victor.training.modulith.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.customer.impl.CustomerRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerModule {
  private final CustomerRepo customerRepo;

  public String getCustomerAddress(String customerId) {
    return customerRepo.findById(customerId).orElseThrow().address();
  }
  public String getCustomerEmail(String customerId) {
    return customerRepo.findById(customerId).orElseThrow().email();
  }

}
