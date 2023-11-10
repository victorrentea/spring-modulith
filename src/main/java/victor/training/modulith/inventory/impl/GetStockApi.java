package victor.training.modulith.inventory.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import victor.training.modulith.common.ProductId;

@RestController
@RequiredArgsConstructor
public class GetStockApi {
  private final StockRepo stockRepo;

  @GetMapping("stock/{productId}")
  @Transactional
  public Integer addStock(@PathVariable ProductId productId) {
    return stockRepo.findByProductId(productId).map(Stock::items).orElse(0);
  }
}
