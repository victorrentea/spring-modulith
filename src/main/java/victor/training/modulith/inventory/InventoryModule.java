package victor.training.modulith.inventory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.impl.*;
import victor.training.modulith.order.OrderStatus;
import victor.training.modulith.order.OrderStatusChangedEvent;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryModule {
  private final ReserveStockService reserveStockService;
  private final StockRepo stockRepo;
  private final StockReservationRepo stockReservationRepo;

  public int getStockValue(long productId) {
    return stockRepo.findByProductId(productId).map(Stock::items).orElse(0);
  }

  public void reserveStock(long orderId, List<LineItem> items) {
    reserveStockService.reserveStock(orderId, items);
  }


  public void fulfillReservation(long orderId) {
      log.info("Stock reservation confirmed: " + orderId);
      stockReservationRepo.deleteAllByOrderId(orderId);
  }
}
