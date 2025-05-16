package victor.training.modulith.shared.api.inventory;

import victor.training.modulith.shared.LineItem;

import java.util.List;

public interface InventoryInternalApi {
  void reserveStock(long orderId, List<LineItem> items);
  void confirmReservation(long orderId);
  StockKnob getStockDetails(long productId);
  //a) it's my module -> change it
  //module managed by another team->
  //b) raise a ticket and block your dev until those son on the beaches do it - waste
  //c) open-source model: I change their code and submit them a dedicated PR that they have to 4-eyes review.⭐️!important
  // via CODEOWNERS file
  int getStock(long productId);
}
