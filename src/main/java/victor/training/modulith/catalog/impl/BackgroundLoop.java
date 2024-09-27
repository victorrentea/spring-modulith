package victor.training.modulith.catalog.impl;

import org.springframework.scheduling.annotation.Scheduled;

public class BackgroundLoop {
  //every second
  @Scheduled(fixedRate = 1000)
  public void loop() {
    // download all the stock levels from the inventory service
  }
}
