package victor.training.modulith.customer.app;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitialData {
  private final CustomerRepo customerRepo;

  @EventListener(ApplicationStartedEvent.class)
  void initialData() {
    customerRepo.save(new Customer(
        "margareta",
        "Margareta",
        "Bucharest",
        "margareta@example.com"));
  }
}
