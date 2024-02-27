package victor.training.modulith.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.customer.impl.Customer;
import victor.training.modulith.customer.impl.CustomerRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerModule {
  private final CustomerRepo customerRepo;

//  public String getPhone(Long customerId) {
//    Customer customer = customerRepo.findById(customerId).orElseThrow();
//    return customer.getPhone();
//  }

//  public Map<Long, String> getPhoneBulk(List<Long> customerId) { //
//    Customer customer = customerRepo.findById(customerId).orElseThrow();
//    return customer.getPhone();
//  }
}
