package victor.training.modulith.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.customer.CustomerModule;
import victor.training.modulith.customer.impl.Customer;
import victor.training.modulith.customer.impl.CustomerRepo;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderModule {

}
