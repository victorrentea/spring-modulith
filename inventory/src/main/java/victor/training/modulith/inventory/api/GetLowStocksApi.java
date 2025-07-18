package victor.training.modulith.inventory.api;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.repo.StockRepo;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetLowStocksApi {
  private final StockRepo stockRepo;

  @GetMapping("stock/low")
  @Transactional
  public List<String> call() {
    var lowStocks = stockRepo.findStockByItemsLessThan(10);
    // TODO return IDs of products low on stock
    //  + CR: get their names (speculate)
    return List.of();
  }
}
