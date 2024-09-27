package victor.training.modulith.catalog.impl;

public class KafkaEventLisnet {
  // @KafkaListener(topics = "OutOfStockEvent")
  public void onOutOfStockEvent(String message) {
    // do something
    // get product from DB, update stock level, save
  }
}
