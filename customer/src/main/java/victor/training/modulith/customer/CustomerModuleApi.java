package victor.training.modulith.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import victor.training.modulith.customer.impl.CustomerRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerModuleApi {
  private final CustomerRepo customerRepo;


}
