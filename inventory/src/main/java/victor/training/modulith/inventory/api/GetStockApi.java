package victor.training.modulith.inventory.api;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.inventory.model.Stock;
import victor.training.modulith.inventory.repo.StockRepo;

@RestController
@RequiredArgsConstructor
public class GetStockApi {
  private final StockRepo stockRepo;

  @GetMapping("stock/{productId}")
  @Transactional
  // different rate of change. don't call from another module
  public Integer call(@PathVariable long productId) {
    return stockRepo.findByProductId(productId).map(Stock::items).orElse(0);
  }
}
