package victor.training.modulith.shared;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogEventsForDebug {
  @EventListener
  public void justLog(Object event) {
    if (event.getClass().getPackageName().contains("victor")) {
      log.debug("Application event published: " +event);
    }
  }
}
