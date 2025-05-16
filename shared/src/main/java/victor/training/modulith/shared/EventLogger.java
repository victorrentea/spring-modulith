package victor.training.modulith.shared;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventLogger {
  @EventListener
  @Order(1000) // don't do this! <<< coupling point
  public void justLog(Object event) {
    if (event.getClass().getPackageName().contains("victor")) {
      log.debug("Application event published: " +event);
    }
  }
}
