package victor.training.modulith.inventory.in.api;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import victor.training.modulith.inventory.app.service.ReserveStockService;
import victor.training.modulith.order.InventoryModuleApi;
import victor.training.modulith.shared.LineItem;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryModule implements InventoryModuleApi {
  private final ReserveStockService reserveStockService;

  public void reserveStock(long orderId, List<LineItem> items) {
    reserveStockService.reserveStock(orderId, items);
  }

}
