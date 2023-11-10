package victor.training.modulith.customer.impl;

import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GetCustomerRest {
  private final CustomerRepo customerRepo;

  @GetMapping("customer/{id}")
  public Customer getCustomerById(@PathVariable String id) {
    return customerRepo.findById(id).orElseThrow();
  }
}
