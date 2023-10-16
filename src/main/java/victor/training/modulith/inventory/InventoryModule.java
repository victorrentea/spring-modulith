package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import victor.training.modulith.inventory.impl.*;
import victor.training.modulith.order.InventoryDoor;
import victor.training.modulith.shared.ProductId;
import victor.training.modulith.order.OrderConfirmedEvent;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class InventoryModule implements InventoryDoor {
  private final StockRepo stockRepo;
  private final StockReservationRepo stockReservationRepo;
  private final ReserveStockService reserveStockService;

  public int getStock(ProductId productId) {
    return stockRepo.findByProductId(productId).map(Stock::items).orElse(0);
  }


  public void reserveStock(long orderId, Map<ProductId, Integer> items) {
    reserveStockService.reserveStock(orderId, items);
  }

}
